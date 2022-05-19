package upt.ac.cti.coverage.flow_insensitive.combiner.field;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.javatuples.Pair;
import upt.ac.cti.coverage.flow_insensitive.combiner.IWritingsCombiner;
import upt.ac.cti.coverage.flow_insensitive.combiner.field.visitor.AFieldWritingsVisitor;
import upt.ac.cti.coverage.flow_insensitive.combiner.field.visitor.CollectionWritingsVisitor;
import upt.ac.cti.coverage.flow_insensitive.combiner.field.visitor.ReferenceWritingsVisitor;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.computation.Either;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;
import upt.ac.cti.util.validation.IsJavaElementCollection;

public class FieldWritingsCombiner implements IWritingsCombiner<IField> {

  private final CodeParser parser = Dependencies.codeParser;
  private final JavaEntitySearcher javaEntitySearcher = Dependencies.javaEntitySearcher;
  private final IsJavaElementCollection<IField> isCollection =
      new IsJavaElementCollection<>(Dependencies.fieldTypeBindingResolver);

  @Override
  public List<Pair<DerivableWriting<IField>, DerivableWriting<IField>>> combine(IField field1, IField field2) {

    Set<IMethod> f1WritingMethods, f2WritingMethods;

    if (!isCollection.test(field1)) {
      f1WritingMethods = javaEntitySearcher.searchFieldWritings(field1);
    } else {
      f1WritingMethods = javaEntitySearcher.searchFieldReferences(field1);
    }

    if (!isCollection.test(field2)) {
      f2WritingMethods = javaEntitySearcher.searchFieldWritings(field2);
    } else {
      f2WritingMethods = javaEntitySearcher.searchFieldReferences(field2);
    }

    var writingBoth = new HashSet<>(f1WritingMethods);
    writingBoth.retainAll(f2WritingMethods);

    var result = new LinkedList<Pair<DerivableWriting<IField>, DerivableWriting<IField>>>();

    writingBoth.forEach(it -> {
      for (DerivableWriting<IField> w1 : getWritings(it, field1)) {
        for (DerivableWriting<IField> w2 : getWritings(it, field2)) {
          result.add(Pair.with(w1, w2));
        }
      }
    });

    f1WritingMethods.removeAll(writingBoth);
    f2WritingMethods.removeAll(writingBoth);

    var f1Writings = f1WritingMethods.stream()
        .flatMap(it -> getWritings(it, field1).stream()).toList();

    var f2Writings = f2WritingMethods.stream()
        .flatMap(it -> getWritings(it, field2).stream()).toList();


    for (DerivableWriting<IField> w1 : f1Writings) {
      for (DerivableWriting<IField> w2 : f2Writings) {
        result.add(Pair.with(w1, w2));
      }
    }

    var init1 = getFieldInitialization(field1);
    var init2 = getFieldInitialization(field2);

    if (init1.isPresent() && init2.isPresent()) {
      result.addFirst(Pair.with(init1.get(), init2.get()));
    }

    return result;
  }

  private List<DerivableWriting<IField>> getWritings(IMethod method, IField field) {
    AFieldWritingsVisitor visitor;

    if (!isCollection.test(field)) {
      visitor = new ReferenceWritingsVisitor(field);
    } else {
      visitor = new CollectionWritingsVisitor(field);
    }


    var nodeOpt = parser.parse(method);
    if (nodeOpt.isPresent()) {
      var node = nodeOpt.get();
      node.accept(visitor);
      return visitor.fieldWritings();

    }
    return List.of();
  }

  private Optional<DerivableWriting<IField>> getFieldInitialization(IField field) {
    if (!isCollection.test(field)) {
      var variableDeclarationFragmentOpt = parser.parse(field);
      return variableDeclarationFragmentOpt
          .flatMap(vdf -> Optional.ofNullable(vdf.getInitializer()))
          .map(initializer -> new DerivableWriting<>(field, initializer,
              Either.left(field.getDeclaringType())));
    }
    return Optional.empty();

  }

}

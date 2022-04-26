package upt.ac.cti.analysis.coverage.flow.insensitive.combiner;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.JavaEntitySearcher;

public class FieldWritingsCombiner implements IFieldWritingsCombiner {

  private final CodeParser parser;
  private final JavaEntitySearcher javaEntitySearcher;

  public FieldWritingsCombiner(CodeParser parser, JavaEntitySearcher javaEntitySearcher) {
    this.parser = parser;
    this.javaEntitySearcher = javaEntitySearcher;
  }

  @Override
  public List<Pair<FieldWriting, FieldWriting>> combine(IField field1, IField field2) {


    var f1WritingMethods = javaEntitySearcher.searchFieldWritings(field1);
    var f2WritingMethods = javaEntitySearcher.searchFieldWritings(field2);

    var writingBoth = new HashSet<>(f1WritingMethods);
    writingBoth.retainAll(f2WritingMethods);

    var result = new LinkedList<Pair<FieldWriting, FieldWriting>>();

    // Combining field writings only from the same method if the respective methods writes both
    // fields
    writingBoth.forEach(it -> {
      var f1Writings = getFieldWritings(it, field1).stream().toList();
      var f2Writings = getFieldWritings(it, field2).stream().toList();

      for (FieldWriting w1 : f1Writings) {
        for (FieldWriting w2 : f2Writings) {
          result.add(Pair.with(w1, w2));
        }
      }
    });

    // Combining writings from separate methods if the repsective methods write only one field
    f1WritingMethods.removeAll(writingBoth);
    f2WritingMethods.removeAll(writingBoth);

    var f1Writings = f1WritingMethods.stream()
        .flatMap(it -> getFieldWritings(it, field1).stream()).toList();

    var f2Writings = f2WritingMethods.stream()
        .flatMap(it -> getFieldWritings(it, field2).stream()).toList();


    for (FieldWriting w1 : f1Writings) {
      for (FieldWriting w2 : f2Writings) {
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

  private List<FieldWriting> getFieldWritings(IMethod method, IField field) {
    var visitor = new FieldWritingsVisitor(field);
    var nodeOpt = parser.parse(method);
    if (nodeOpt.isPresent()) {
      var node = nodeOpt.get();
      node.accept(visitor);
      return visitor.result();

    }
    return List.of();
  }

  private Optional<FieldWriting> getFieldInitialization(IField field) {
    var variableDeclarationFragmentOpt = parser.parse(field);
    return variableDeclarationFragmentOpt
        .flatMap(vdf -> Optional.ofNullable(vdf.getInitializer()))
        .map(initializer -> new FieldWriting(field, initializer, Optional.empty()));

  }

}

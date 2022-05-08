package upt.ac.cti.coverage.derivator.derivation.shared.algorithm;

import java.util.LinkedList;
import java.util.Optional;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.Expression;
import org.javatuples.Pair;
import upt.ac.cti.coverage.derivator.derivation.shared.visitor.FieldAssignmentVisitor;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public final class FieldDerivatorAlgorithm<J extends IJavaElement> {

  private final JavaEntitySearcher javaEntitySearcher;
  private final CodeParser codeParser;

  public FieldDerivatorAlgorithm(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.javaEntitySearcher = javaEntitySearcher;
    this.codeParser = codeParser;
  }

  public NewWritingPairs<J> derive(Writing<J> deriver, Writing<J> constant, IField field) {
    var writingMethods = javaEntitySearcher.searchFieldWritings(field);


    var derivationsStream = writingMethods.stream()
        .map(method -> codeParser.parse(method))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .flatMap(methodDeclaration -> {
          var visitor = new FieldAssignmentVisitor<>(deriver, field);
          var node = methodDeclaration.getBody();
          node.accept(visitor);
          return visitor.derivations().stream().map(Writing::increaseDepth);
        });

    var derivations = new LinkedList<>(derivationsStream.toList());

    var init = getFieldInitialization(field);
    if (init.isPresent()) {

      derivations.add(deriver.withWritingExpression(init.get()));
    }

    var newWritingPairs = derivations.stream()
        .map(derivation -> Pair.with(derivation, constant))
        .toList();;

    return new NewWritingPairs<>(newWritingPairs);
  }

  private Optional<Expression> getFieldInitialization(IField field) {
    var variableDeclarationFragmentOpt = codeParser.parse(field);
    return variableDeclarationFragmentOpt
        .flatMap(vdf -> Optional.ofNullable(vdf.getInitializer()));

  }
}

package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal;

import java.util.Optional;
import org.eclipse.jdt.core.IField;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.visitors.FieldAssignmentVisitor;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.NewWritingPairs;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.JavaEntitySearcher;

final class NameFieldDerivator implements IFieldWritingsDerivator {

  private final JavaEntitySearcher javaEntitySearcher;
  private final CodeParser codeParser;

  public NameFieldDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.javaEntitySearcher = javaEntitySearcher;
    this.codeParser = codeParser;
  }


  @Override
  public DerivationResult derive(FieldWriting deriver, FieldWriting constant) {
    var field = (IField) deriver.writingExpression().resolveTypeBinding().getJavaElement();

    var wirtingMethods = javaEntitySearcher.searchFieldWritings(field);

    var writingExpressions = wirtingMethods.stream()
        .map(method -> codeParser.parse(method))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .flatMap(methodDeclaration -> {
          var visitor = new FieldAssignmentVisitor(field);
          var node = methodDeclaration.getBody();
          node.accept(visitor);
          return visitor.result().stream();
        }).toList();

    var newWritingPairs = writingExpressions.stream()
        .map(writingExpression -> {
          var derivation = deriver.withWritingExpression(writingExpression);
          return Pair.with(derivation, constant);
        })
        .toList();;

    return new NewWritingPairs(newWritingPairs);
  }


}


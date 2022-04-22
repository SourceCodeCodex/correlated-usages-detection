package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.visitors.MethodReturnVisitor;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.NewWritingPairs;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;

final class MethodInvocationDerivator implements IFieldWritingsDerivator {

  private final CodeParser codeParser;

  public MethodInvocationDerivator(CodeParser codeParser) {
    this.codeParser = codeParser;
  }

  @Override
  public DerivationResult derive(FieldWriting deriver, FieldWriting constant) {
    var methodInvocation = (MethodInvocation) deriver.writingExpression();
    var method = (IMethod) methodInvocation.resolveMethodBinding().getJavaElement();

    var node = codeParser.parse(method);
    if (node.isEmpty()) {
      return NewWritingPairs.NULL;
    }
    var visitor = new MethodReturnVisitor();
    node.get().accept(visitor);
    var returnExpressions = visitor.result();

    var newWritingPairs = returnExpressions.stream()
        .map(returnExpression -> {
          var derivation = deriver.withWritingExpression(returnExpression);
          return Pair.with(derivation, constant);
        })
        .toList();;

    return new NewWritingPairs(newWritingPairs);
  }
}



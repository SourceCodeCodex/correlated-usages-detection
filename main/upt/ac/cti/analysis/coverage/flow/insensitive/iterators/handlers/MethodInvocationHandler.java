package upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers;

import java.util.Optional;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.MethodInvocation;
import upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers.visitors.MethodReturnVisitor;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CPIndex;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CorelationPair;

// TODO: Should the object change when invoking a method?
final class MethodInvocationHandler extends RightSideHandler {

  public MethodInvocationHandler(CorelationPair cp, CPIndex index) {
    super(cp, index);
  }

  @Override
  public DerivationResult handle() {
    var invoc = (MethodInvocation) cp.fieldAsgmt(index).rightSide();
    var iMethod = (IMethod) invoc.resolveMethodBinding().getJavaElement();

    var visitor = new MethodReturnVisitor();
    var node = CodeParser.instance().parse(iMethod);
    node.accept(visitor);
    var returns = visitor.result();

    var newPairs = returns.stream()
        .map(expr -> {
          var updatedExpr = cp.fieldAsgmt(index).withExpression(expr);
          return cp.withFieldAsgmt(updatedExpr, index);
        })
        .toList();;

    return new DerivationResult(newPairs, Optional.empty());
  }
}



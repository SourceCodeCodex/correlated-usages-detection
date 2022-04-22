package upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers;

import java.util.Optional;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers.visitors.MethodReturnVisitor;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CPIndex;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CorelationPair;

final class SuperMethodInvocationHandler extends RightSideHandler {

  public SuperMethodInvocationHandler(CorelationPair cp, CPIndex index) {
    super(cp, index);
  }

  @Override
  public DerivationResult handle() {
    var invoc = (SuperMethodInvocation) cp.fieldAsgmt(index).rightSide();
    var iMethod = (IMethod) invoc.resolveMethodBinding().getJavaElement();

    var visitor = new MethodReturnVisitor();
    var node = CodeParser.instance().parse(iMethod);
    node.accept(visitor);
    var returns = visitor.result();

    var newPairs = returns.stream()
        .map(expr -> {
          var updatedRight = cp.fieldAsgmt(index).withExpression(expr);
          return cp.withFieldAsgmt(updatedRight, index);
        })
        .toList();;

    return new DerivationResult(newPairs, Optional.empty());
  }
}



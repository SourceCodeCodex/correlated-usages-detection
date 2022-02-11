package upt.ac.cti.coverage.analysis.flow.insensitive.iterators.handlers;

import java.util.Optional;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.MethodInvocation;
import upt.ac.cti.coverage.analysis.flow.insensitive.iterators.handlers.visitors.MethodReturnVisitor;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPHandlingResult;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPIndex;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CorelationPair;
import upt.ac.cti.utils.parsers.CachedParser;

// TODO: Should the object change when invoking a method?
final class MethodInvocationHandler extends RightSideHandler {

  public MethodInvocationHandler(CorelationPair cp, CPIndex index) {
    super(cp, index);
  }

  @Override
  public CPHandlingResult handle() {
    var invoc = (MethodInvocation) cp.fieldAsgmt(index).rightSide();
    var iMethod = (IMethod) invoc.resolveMethodBinding().getJavaElement();

    var visitor = new MethodReturnVisitor();
    var node = CachedParser.instance().parse(iMethod);
    node.accept(visitor);
    var returns = visitor.result();

    var newPairs = returns.stream()
        .map(expr -> {
          var updatedExpr = cp.fieldAsgmt(index).withRightSide(expr).withCurrentMethod(iMethod);
          return cp.withFieldAsgmt(updatedExpr, index);
        })
        .toList();;

    return new CPHandlingResult(newPairs, Optional.empty());
  }
}



package upt.ac.cti.coverage.analysis.flow.insensitive.iterators.handlers;

import java.util.Optional;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import upt.ac.cti.coverage.analysis.flow.insensitive.iterators.handlers.visitors.MethodInvocationVisitor;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPHandlingResult;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPIndex;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CorelationPair;
import upt.ac.cti.utils.parsers.CachedParser;
import upt.ac.cti.utils.searchers.MethodInvocationSearcher;


// TODO: REFINE HOW BASE OBJECT CHANGES
// TODO: WHEN CHANGING THE BASE OBJECT, USING THE CURRENT ALGORITHM
// TODO: THE NEW PAIR IS REMOVED IMMEDIATELY
final public class NameParameterHandler extends RightSideHandler {
  private final ILocalVariable parameter;

  public NameParameterHandler(CorelationPair cp, CPIndex index, ILocalVariable parameter) {
    super(cp, index);
    this.parameter = parameter;
  }

  @Override
  public CPHandlingResult handle() {
    var invocations = MethodInvocationSearcher.instance()
        .searchFieldWrites((IMethod) parameter.getDeclaringMember());

    System.out.println(invocations);

    var parameterExpr = invocations.stream().flatMap(it -> {
      var visitor = new MethodInvocationVisitor(parameter);
      var node = CachedParser.instance().parse(it.getCompilationUnit());

      node.accept(visitor);
      return visitor.result().stream();
    }).toList();

    var newPairs = parameterExpr.stream()
        .map(expr -> {
          var updatedRight = cp.fieldAsgmt(index).withRightSide(expr);
          return cp.withFieldAsgmt(updatedRight, index);
        })
        .toList();;

    return new CPHandlingResult(newPairs, Optional.empty());
  }

}

package upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers;

import java.util.Optional;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers.visitors.MethodInvocationVisitor;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.MethodInvocationSearcher;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CPIndex;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CorelationPair;


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
  public DerivationResult handle() {
    var invocations = MethodInvocationSearcher.instance()
        .searchFieldWrites((IMethod) parameter.getDeclaringMember());

    var parameterExpr = invocations.stream().flatMap(it -> {
      var visitor = new MethodInvocationVisitor(parameter);
      var node = CodeParser.instance().parse(it.getCompilationUnit());

      node.accept(visitor);
      return visitor.result().stream();
    }).toList();

    var newPairs = parameterExpr.stream()
        .map(expr -> {
          var updatedRight = cp.fieldAsgmt(index).withExpression(expr);
          return cp.withFieldAsgmt(updatedRight, index);
        })
        .toList();;

    return new DerivationResult(newPairs, Optional.empty());
  }

}

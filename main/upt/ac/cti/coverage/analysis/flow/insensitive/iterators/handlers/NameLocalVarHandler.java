package upt.ac.cti.coverage.analysis.flow.insensitive.iterators.handlers;

import java.util.Optional;
import org.eclipse.jdt.core.ILocalVariable;
import upt.ac.cti.coverage.analysis.flow.insensitive.iterators.handlers.visitors.LocalVarAsgmtVisitor;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPHandlingResult;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPIndex;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CorelationPair;
import upt.ac.cti.utils.parsers.CachedParser;
import upt.ac.cti.utils.searchers.WritesSearcher;

final class NameLocalVarHandler extends RightSideHandler {
  private final ILocalVariable localVar;

  public NameLocalVarHandler(CorelationPair cp, CPIndex index, ILocalVariable localVar) {
    super(cp, index);
    this.localVar = localVar;
  }

  @Override
  public CPHandlingResult handle() {
    var assignmentMethods = WritesSearcher.instance().searchLocalVariablesWrites(localVar);

    var rightExpressions = assignmentMethods.stream().flatMap(it -> {
      var visitor = new LocalVarAsgmtVisitor(localVar);
      var node = CachedParser.instance().parse(it).getBody();
      node.accept(visitor);
      return visitor.result().stream();
    }).toList();

    var newPairs = rightExpressions.stream()
        .map(expr -> {
          var updatedRight = cp.fieldAsgmt(index).withRightSide(expr);
          return cp.withFieldAsgmt(updatedRight, index);
        })
        .toList();;

    return new CPHandlingResult(newPairs, Optional.empty());
  }

}

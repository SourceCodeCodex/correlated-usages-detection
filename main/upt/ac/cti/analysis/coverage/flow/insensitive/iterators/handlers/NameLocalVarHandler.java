package upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers;

import java.util.Optional;
import org.eclipse.jdt.core.ILocalVariable;
import upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers.visitors.LocalVarAsgmtVisitor;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.WritingsSearcher;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CPIndex;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CorelationPair;

final class NameLocalVarHandler extends RightSideHandler {
  private final ILocalVariable localVar;

  public NameLocalVarHandler(CorelationPair cp, CPIndex index, ILocalVariable localVar) {
    super(cp, index);
    this.localVar = localVar;
  }

  @Override
  public DerivationResult handle() {
    var assignmentMethods = WritingsSearcher.instance().searchLocalVariablesWrites(localVar);

    var rightExpressions = assignmentMethods.stream().flatMap(it -> {
      var visitor = new LocalVarAsgmtVisitor(localVar);
      var node = CodeParser.instance().parse(it).getBody();
      node.accept(visitor);
      return visitor.result().stream();
    }).toList();

    var newPairs = rightExpressions.stream()
        .map(expr -> {
          var updatedRight = cp.fieldAsgmt(index).withExpression(expr);
          return cp.withFieldAsgmt(updatedRight, index);
        })
        .toList();;

    return new DerivationResult(newPairs, Optional.empty());
  }

}

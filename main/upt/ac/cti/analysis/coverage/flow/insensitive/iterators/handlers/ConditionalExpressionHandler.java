package upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CPIndex;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CorelationPair;

final class ConditionalExpressionHandler extends RightSideHandler {

  public ConditionalExpressionHandler(CorelationPair cp, CPIndex index) {
    super(cp, index);
  }

  @Override
  public DerivationResult handle() {
    var cond = (ConditionalExpression) cp.field1Asgmt().rightSide();

    var updatedRight1 = cp.fieldAsgmt(index).withExpression(cond.getThenExpression());
    var newPair1 = cp.withFieldAsgmt(updatedRight1, index);

    var updatedRight2 = cp.fieldAsgmt(index).withExpression(cond.getElseExpression());
    var newPair2 = cp.withFieldAsgmt(updatedRight2, index);

    return new DerivationResult(List.of(newPair1, newPair2), Optional.empty());
  }
}

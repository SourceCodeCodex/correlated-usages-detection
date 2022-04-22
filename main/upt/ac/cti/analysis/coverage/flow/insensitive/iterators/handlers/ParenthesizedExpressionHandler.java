package upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CPIndex;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CorelationPair;

final class ParenthesizedExpressionHandler extends RightSideHandler {

  public ParenthesizedExpressionHandler(CorelationPair cp, CPIndex index) {
    super(cp, index);
  }

  @Override
  public DerivationResult handle() {
    var par = (ParenthesizedExpression) cp.field1Asgmt().rightSide();
    var updatedRight = cp.fieldAsgmt(index).withExpression(par.getExpression());
    var newPair = cp.withFieldAsgmt(updatedRight, index);
    return new DerivationResult(List.of(newPair), Optional.empty());
  }
}

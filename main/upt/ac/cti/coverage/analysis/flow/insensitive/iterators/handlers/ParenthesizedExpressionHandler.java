package upt.ac.cti.coverage.analysis.flow.insensitive.iterators.handlers;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPHandlingResult;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPIndex;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CorelationPair;

final class ParenthesizedExpressionHandler extends RightSideHandler {

  public ParenthesizedExpressionHandler(CorelationPair cp, CPIndex index) {
    super(cp, index);
  }

  @Override
  public CPHandlingResult handle() {
    var par = (ParenthesizedExpression) cp.field1Asgmt().rightSide();
    var updatedRight = cp.fieldAsgmt(index).withRightSide(par.getExpression());
    var newPair = cp.withFieldAsgmt(updatedRight, index);
    return new CPHandlingResult(List.of(newPair), Optional.empty());
  }
}

package upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.Assignment;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CPIndex;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CorelationPair;

final class AssignmentHandler extends RightSideHandler {
  public AssignmentHandler(CorelationPair cp, CPIndex index) {
    super(cp, index);
  }

  @Override
  public DerivationResult handle() {
    var asgmt = (Assignment) cp.field1Asgmt().rightSide();
    var updatedRight = cp.fieldAsgmt(index).withExpression(asgmt.getRightHandSide());
    var newPair = cp.withFieldAsgmt(updatedRight, index);
    return new DerivationResult(List.of(newPair), Optional.empty());
  }

}


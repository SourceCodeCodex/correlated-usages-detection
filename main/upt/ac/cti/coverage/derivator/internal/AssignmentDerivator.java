package upt.ac.cti.coverage.derivator.internal;

import org.eclipse.jdt.core.dom.Assignment;
import org.javatuples.Pair;
import upt.ac.cti.coverage.model.DerivationResult;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.NewWritingPairs;

final class AssignmentDerivator implements IFieldWritingsDerivator {

  @Override
  public DerivationResult derive(Writing deriver, Writing constant) {
    var assignment = (Assignment) deriver.writingExpression();
    var derivation = deriver.withWritingExpression(assignment.getRightHandSide());
    var newWritingsPair = Pair.with(derivation, constant);
    return new NewWritingPairs(newWritingsPair);
  }

}


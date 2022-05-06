package upt.ac.cti.coverage.derivator.derivation.simple;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.Assignment;
import org.javatuples.Pair;
import upt.ac.cti.coverage.derivator.derivation.IWritingsDerivator;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;

final class AssignmentDerivator<J extends IJavaElement> implements IWritingsDerivator<J> {

  @Override
  public NewWritingPairs<J> derive(Writing<J> deriver, Writing<J> constant) {
    var assignment = (Assignment) deriver.writingExpression();
    var derivation = deriver.withWritingExpression(assignment.getRightHandSide());
    var newWritingsPair = Pair.with(derivation, constant);
    return NewWritingPairs.of(newWritingsPair);
  }

}


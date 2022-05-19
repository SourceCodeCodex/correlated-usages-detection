package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.simple;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.Assignment;
import org.javatuples.Pair;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.ISimpleWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;

final class AssignmentDerivator<J extends IJavaElement> implements ISimpleWritingsDerivator<J> {

  @Override
  public NewWritingPairs<J> derive(DerivableWriting<J> deriver, Writing<J> constant) {
    var assignment = (Assignment) deriver.writingExpression();
    var derivation = deriver.withWritingExpression(assignment.getRightHandSide());
    var newWritingsPair = Pair.with(derivation, constant);
    return NewWritingPairs.of(newWritingsPair);
  }

}


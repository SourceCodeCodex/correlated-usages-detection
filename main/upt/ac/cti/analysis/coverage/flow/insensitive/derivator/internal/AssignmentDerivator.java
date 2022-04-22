package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal;

import org.eclipse.jdt.core.dom.Assignment;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.NewWritingPairs;

final class AssignmentDerivator implements IFieldWritingsDerivator {

  @Override
  public DerivationResult derive(FieldWriting deriver, FieldWriting constant) {
    var assignment = (Assignment) deriver.writingExpression();
    var derivation = deriver.withWritingExpression(assignment.getRightHandSide());
    var newWritingsPair = Pair.with(derivation, constant);
    return new NewWritingPairs(newWritingsPair);
  }

}


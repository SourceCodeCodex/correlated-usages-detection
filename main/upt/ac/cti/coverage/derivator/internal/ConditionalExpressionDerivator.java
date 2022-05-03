package upt.ac.cti.coverage.derivator.internal;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.javatuples.Pair;
import upt.ac.cti.coverage.model.DerivationResult;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.NewWritingPairs;

final class ConditionalExpressionDerivator implements IFieldWritingsDerivator {

  @Override
  public DerivationResult derive(Writing deriver, Writing constant) {
    var conditional = (ConditionalExpression) deriver.writingExpression();

    var derivation1 = deriver.withWritingExpression(conditional.getThenExpression());
    var newWritingsPair1 = Pair.with(derivation1, constant);

    var derivation2 = deriver.withWritingExpression(conditional.getElseExpression());
    var newWritingsPair2 = Pair.with(derivation2, constant);

    return new NewWritingPairs(newWritingsPair1, newWritingsPair2);
  }
}

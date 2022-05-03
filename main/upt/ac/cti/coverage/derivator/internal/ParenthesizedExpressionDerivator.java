package upt.ac.cti.coverage.derivator.internal;

import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.javatuples.Pair;
import upt.ac.cti.coverage.model.DerivationResult;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.NewWritingPairs;

final class ParenthesizedExpressionDerivator implements IFieldWritingsDerivator {

  @Override
  public DerivationResult derive(Writing deriver, Writing constant) {
    var par = (ParenthesizedExpression) deriver.writingExpression();
    var derivation = deriver.withWritingExpression(par.getExpression());
    var newWritingsPair = Pair.with(derivation, constant);
    return new NewWritingPairs(newWritingsPair);
  }
}

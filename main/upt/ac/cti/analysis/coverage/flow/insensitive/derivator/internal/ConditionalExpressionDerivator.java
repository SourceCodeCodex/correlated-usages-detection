package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.NewWritingPairs;

final class ConditionalExpressionDerivator implements IFieldWritingsDerivator {

  @Override
  public DerivationResult derive(FieldWriting deriver, FieldWriting constant) {
    var conditional = (ConditionalExpression) deriver.writingExpression();

    var derivation1 = deriver.withWritingExpression(conditional.getThenExpression());
    var newWritingsPair1 = Pair.with(derivation1, constant);

    var derivation2 = deriver.withWritingExpression(conditional.getElseExpression());
    var newWritingsPair2 = Pair.with(derivation2, constant);

    return new NewWritingPairs(newWritingsPair1, newWritingsPair2);
  }
}

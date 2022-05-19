package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.simple;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.javatuples.Pair;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.ISimpleWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;

final class ConditionalExpressionDerivator<J extends IJavaElement>
    implements ISimpleWritingsDerivator<J> {

  @Override
  public NewWritingPairs<J> derive(DerivableWriting<J> deriver, Writing<J> constant) {
    var conditional = (ConditionalExpression) deriver.writingExpression();

    var derivation1 = deriver.withWritingExpression(conditional.getThenExpression());
    var newWritingsPair1 = Pair.with(derivation1, constant);

    var derivation2 = deriver.withWritingExpression(conditional.getElseExpression());
    var newWritingsPair2 = Pair.with(derivation2, constant);

    return NewWritingPairs.of(newWritingsPair1, newWritingsPair2);
  }
}

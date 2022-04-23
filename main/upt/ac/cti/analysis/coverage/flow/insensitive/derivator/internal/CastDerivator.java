package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal;

import org.eclipse.jdt.core.dom.CastExpression;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.NewWritingPairs;

final class CastDerivator implements IFieldWritingsDerivator {

  @Override
  public DerivationResult derive(FieldWriting deriver, FieldWriting constant) {
    var cast = (CastExpression) deriver.writingExpression();
    var derivation = deriver.withWritingExpression(cast.getExpression());
    var newWritingsPair = Pair.with(derivation, constant);
    return new NewWritingPairs(newWritingsPair);
  }

}


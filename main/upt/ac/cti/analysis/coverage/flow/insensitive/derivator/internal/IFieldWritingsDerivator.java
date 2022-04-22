package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal;

import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;

public interface IFieldWritingsDerivator {

  public abstract DerivationResult derive(FieldWriting deriver, FieldWriting constant);

}

package upt.ac.cti.coverage.derivator.internal;

import upt.ac.cti.coverage.model.DerivationResult;
import upt.ac.cti.coverage.model.Writing;

public interface IFieldWritingsDerivator {

  public abstract DerivationResult derive(Writing deriver, Writing constant);

}

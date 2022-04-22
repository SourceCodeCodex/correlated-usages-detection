package upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers;

import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CPIndex;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CorelationPair;

abstract class RightSideHandler {

  protected final CorelationPair cp;
  protected final CPIndex index;

  public RightSideHandler(CorelationPair cp, CPIndex index) {
    this.cp = cp;
    this.index = index;
  }

  public abstract DerivationResult handle();

}

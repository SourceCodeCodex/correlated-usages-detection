package upt.ac.cti.coverage.analysis.flow.insensitive.iterators.handlers;

import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPHandlingResult;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPIndex;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CorelationPair;

abstract class RightSideHandler {

  protected final CorelationPair cp;
  protected final CPIndex index;

  public RightSideHandler(CorelationPair cp, CPIndex index) {
    this.cp = cp;
    this.index = index;
  }

  public abstract CPHandlingResult handle();

}

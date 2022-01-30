package upt.ac.cti.coverage.analysis.iterative.iterators.handlers;

import java.util.List;
import java.util.Optional;
import upt.ac.cti.coverage.analysis.iterative.model.CPHandlingResult;
import upt.ac.cti.coverage.analysis.iterative.model.CorelationPair;

abstract class RightSideHandler {

  protected final CorelationPair cp;

  public RightSideHandler(CorelationPair cp) {
    this.cp = cp;
  }

  public abstract CPHandlingResult handle();


  // TODO: DELETE WHEN ALL IMPLEMENTED
  protected CPHandlingResult voidResult() {
    return new CPHandlingResult(List.of(), Optional.empty());
  }

}

package upt.ac.cti.coverage.analysis.iterative.generators;

import java.util.List;
import upt.ac.cti.coverage.analysis.iterative.model.CorelationPair;

public interface CPGenerator {
  public List<CorelationPair> generate();

}

package upt.ac.cti.coverage.analysis.flow.insensitive.generators;

import java.util.List;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CorelationPair;

public interface CPGenerator {
  public List<CorelationPair> generate();

}

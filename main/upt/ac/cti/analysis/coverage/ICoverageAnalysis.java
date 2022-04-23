package upt.ac.cti.analysis.coverage;

import java.util.List;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;

public interface ICoverageAnalysis {
  public List<Pair<IType, IType>> coveredTypes();
}

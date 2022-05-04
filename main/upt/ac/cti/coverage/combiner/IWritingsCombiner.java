package upt.ac.cti.coverage.combiner;

import java.util.List;
import org.eclipse.jdt.core.IJavaElement;
import org.javatuples.Pair;
import upt.ac.cti.coverage.model.Writing;

public interface IWritingsCombiner<J extends IJavaElement> {

  public List<Pair<Writing, Writing>> combine(J javaElement1, J javaElement2);

}

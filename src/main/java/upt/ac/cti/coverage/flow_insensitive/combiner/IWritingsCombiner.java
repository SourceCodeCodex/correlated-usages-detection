package upt.ac.cti.coverage.flow_insensitive.combiner;

import java.util.List;
import org.eclipse.jdt.core.IJavaElement;
import org.javatuples.Pair;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;

public interface IWritingsCombiner<J extends IJavaElement> {

  public List<Pair<DerivableWriting<J>, DerivableWriting<J>>> combine(J javaElement1, J javaElement2);

}

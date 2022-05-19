package upt.ac.cti.coverage.flow_insensitive.derivator;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;

public interface IDerivationManager<J extends IJavaElement> {
  public Optional<Set<Pair<IType, IType>>> derive(List<Pair<DerivableWriting<J>, DerivableWriting<J>>> input);
}

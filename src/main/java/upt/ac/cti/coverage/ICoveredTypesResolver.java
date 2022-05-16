package upt.ac.cti.coverage;

import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;


public interface ICoveredTypesResolver<J extends IJavaElement> {

  Optional<Set<Pair<IType, IType>>> resolve(J javaElement1, J javaElement2);

}

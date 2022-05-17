package upt.ac.cti.coverage.flow_insensitive;

import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.cache.CacheRegion;

public final class ConservingFlowInsensitiveParameterCoveredTypesResolver
    extends AFlowInsensitiveCoveredTypesResolver<ILocalVariable> {

  public ConservingFlowInsensitiveParameterCoveredTypesResolver() {
    super(Dependencies.parameterWritingsCombiner,
        Dependencies.parameterConservingDerivationManager,
        CacheRegion.COVERED_TYPES_CFI);
  }

  @Override
  public Optional<Set<Pair<IType, IType>>> resolve(ILocalVariable param1, ILocalVariable param2) {
    return super.resolve(param1, param2);
  }

}

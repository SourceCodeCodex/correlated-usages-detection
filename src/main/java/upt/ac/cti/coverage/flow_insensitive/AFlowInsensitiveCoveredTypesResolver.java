package upt.ac.cti.coverage.flow_insensitive;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.javatuples.Pair;
import upt.ac.cti.coverage.ICoveredTypesResolver;
import upt.ac.cti.coverage.flow_insensitive.combiner.IWritingsCombiner;
import upt.ac.cti.coverage.flow_insensitive.derivator.IDerivationManager;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.cache.CacheRegion;

abstract class AFlowInsensitiveCoveredTypesResolver<J extends IJavaElement>
    implements ICoveredTypesResolver<J> {

  private final IWritingsCombiner<J> writingsCombiner;
  private final IDerivationManager<J> derivationManager;

  private final Cache<Pair<J, J>, Optional<Set<Pair<IType, IType>>>> cache;

  public AFlowInsensitiveCoveredTypesResolver(
      IWritingsCombiner<J> writingsCombiner,
      IDerivationManager<J> derivationManager,
      CacheRegion region) {
    this.writingsCombiner = writingsCombiner;
    this.derivationManager = derivationManager;
    cache = new Cache<>(region);
  }

  @Override
  public Optional<Set<Pair<IType, IType>>> resolve(J javaElement1, J javaElement2) {
    var cached = cache.get(Pair.with(javaElement1, javaElement2));
    if (cached.isPresent()) {
      return cached.get();
    }

    var writingPairs = writingsCombiner.combine(javaElement1, javaElement2);

    var result = derivationManager.derive(writingPairs).map(s -> s.stream()
        .filter(p -> {
          try {
            return !(p.getValue0().isAnonymous() || p.getValue1().isAnonymous());
          } catch (JavaModelException e) {
            e.printStackTrace();
            return false;
          }
        }).collect(Collectors.toSet()));
    cache.put(Pair.with(javaElement1, javaElement2), result);

    return result;
  }

}

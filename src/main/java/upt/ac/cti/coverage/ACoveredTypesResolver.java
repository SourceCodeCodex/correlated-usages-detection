package upt.ac.cti.coverage;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.javatuples.Pair;
import upt.ac.cti.aperture.AAllTypePairsResolver;
import upt.ac.cti.coverage.combiner.IWritingsCombiner;
import upt.ac.cti.coverage.derivator.DerivationManager;
import upt.ac.cti.coverage.derivator.util.AWritingBindingResolver;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.cache.CacheRegions;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

abstract class ACoveredTypesResolver<J extends IJavaElement> {

  private final CodeParser codeParser;
  private final JavaEntitySearcher javaEntitySearcher;
  private final IWritingsCombiner<J> writingsCombiner;
  private final AWritingBindingResolver<J> aWritingBindingResolver;
  private final AAllTypePairsResolver<J> aAllTypePairsResolver;

  private final Cache<Pair<J, J>, Optional<Set<Pair<IType, IType>>>> cache =
      new Cache<>(CacheRegions.COVERED_TYPES);

  public ACoveredTypesResolver(
      IWritingsCombiner<J> writingsCombiner,
      CodeParser codeParser,
      JavaEntitySearcher javaEntitySearcher,
      AAllTypePairsResolver<J> aAllTypePairsResolver,
      AWritingBindingResolver<J> aWritingBindingResolver) {
    this.writingsCombiner = writingsCombiner;
    this.aWritingBindingResolver = aWritingBindingResolver;
    this.codeParser = codeParser;
    this.javaEntitySearcher = javaEntitySearcher;
    this.aAllTypePairsResolver = aAllTypePairsResolver;
  }

  protected Optional<Set<Pair<IType, IType>>> resolve(J javaElement1, J javaElement2) {
    var cached = cache.get(Pair.with(javaElement1, javaElement2));
    if (cached.isPresent()) {
      return cached.get();
    }

    var writingPairs = writingsCombiner.combine(javaElement1, javaElement2);

    var deriver =
        new DerivationManager<>(
            aWritingBindingResolver,
            javaEntitySearcher,
            codeParser,
            aAllTypePairsResolver);

    var result = deriver.derive(writingPairs).map(s -> s.stream()
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

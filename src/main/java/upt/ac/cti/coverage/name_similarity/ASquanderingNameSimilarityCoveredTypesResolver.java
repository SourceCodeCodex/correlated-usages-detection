package upt.ac.cti.coverage.name_similarity;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;
import upt.ac.cti.aperture.AAllTypePairsResolver;
import upt.ac.cti.util.binding.ABindingResolver;
import upt.ac.cti.util.cache.CacheRegion;

abstract class ASquanderingNameSimilarityCoveredTypesResolver<J extends IJavaElement>
    extends ANameSimilarityCoveredTypesResolver<J> {


  public ASquanderingNameSimilarityCoveredTypesResolver(
      ABindingResolver<J, ITypeBinding> aBindingResolver,
      AAllTypePairsResolver<J> aAllTypePairsResolver) {
    super(aBindingResolver, aAllTypePairsResolver, CacheRegion.COVERED_TYPES_SNS);
  }

  @Override
  protected Optional<Set<Pair<IType, IType>>> computeResult(J javaElement1, J javaElement2) {
    var rootTokens = rootTokens(javaElement1, javaElement2);
    return Optional.of(aAllTypePairsResolver.resolve(javaElement1, javaElement2).parallelStream()
        .filter(p -> validateTokens(p, rootTokens))
        .collect(Collectors.toSet()));
  }

}



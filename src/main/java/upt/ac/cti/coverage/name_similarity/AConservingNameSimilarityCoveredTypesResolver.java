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

abstract class AConservingNameSimilarityCoveredTypesResolver<J extends IJavaElement>
    extends ANameSimilarityCoveredTypesResolver<J> {


  public AConservingNameSimilarityCoveredTypesResolver(
      ABindingResolver<J, ITypeBinding> aBindingResolver,
      AAllTypePairsResolver<J> aAllTypePairsResolver) {
    super(aBindingResolver, aAllTypePairsResolver, CacheRegion.COVERED_TYPES_CNS);
  }

  @Override
  protected Optional<Set<Pair<IType, IType>>> computeResult(J javaElement1, J javaElement2) {

    var rootTokens = rootTokens(javaElement1, javaElement2);

    var t1Types = aAllTypePairsResolver.resolve(javaElement1);
    final var t2Types = aAllTypePairsResolver.resolve(javaElement2);

    var result = t1Types.stream()
        .flatMap(t1 -> {
          var comaptible =
              t2Types.stream()
                  .map(t2 -> Pair.with(t1, t2))
                  .filter(p -> validateTokens(p, rootTokens)).toList();

          if (!comaptible.isEmpty()) {
            return comaptible.stream();
          }
          return t2Types.stream()
              .map(t2 -> Pair.with(t1, t2));
        })
        .collect(Collectors.toSet());

    return Optional.of(result);

  }

}



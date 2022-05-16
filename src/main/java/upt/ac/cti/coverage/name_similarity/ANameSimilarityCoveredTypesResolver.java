package upt.ac.cti.coverage.name_similarity;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;
import upt.ac.cti.aperture.AAllTypePairsResolver;
import upt.ac.cti.config.Config;
import upt.ac.cti.coverage.ICoveredTypesResolver;
import upt.ac.cti.util.binding.ABindingResolver;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.cache.CacheRegions;
import upt.ac.cti.util.computation.TokensUtil;
import upt.ac.cti.util.validation.IsTypeBindingCollection;

abstract class ANameSimilarityCoveredTypesResolver<J extends IJavaElement>
    implements ICoveredTypesResolver<J> {

  private final ABindingResolver<J, ITypeBinding> aBindingResolver;
  private final AAllTypePairsResolver<J> aAllTypePairsResolver;

  private static final IsTypeBindingCollection isCollection = new IsTypeBindingCollection();

  private static final int TOKENS_MAX_DIFF = Config.TOKENS_MAX_DIFF;;
  private static final double TOKENS_THRESHOLD = Config.TOKENS_THRESHOLD;;

  private final Cache<Pair<J, J>, Optional<Set<Pair<IType, IType>>>> cache =
      new Cache<>(CacheRegions.COVERED_TYPES_NAME_SIMILARITY);

  public ANameSimilarityCoveredTypesResolver(
      ABindingResolver<J, ITypeBinding> aBindingResolver,
      AAllTypePairsResolver<J> aAllTypePairsResolver) {
    this.aBindingResolver = aBindingResolver;
    this.aAllTypePairsResolver = aAllTypePairsResolver;
  }

  @Override
  public Optional<Set<Pair<IType, IType>>> resolve(J javaElement1, J javaElement2) {
    var cached = cache.get(Pair.with(javaElement1, javaElement2));
    if (cached.isPresent()) {
      return cached.get();
    }

    var typeBinding1O = aBindingResolver.resolve(javaElement1);
    var typeBinding2O = aBindingResolver.resolve(javaElement2);

    if (typeBinding1O.isEmpty() || typeBinding2O.isEmpty()) {
      return Optional.empty();
    }

    var tb1 = typeBinding1O.get();
    var tb2 = typeBinding2O.get();

    if (isCollection.test(tb1)) {
      tb1 = tb1.getTypeArguments()[0];
    }

    if (isCollection.test(tb2)) {
      tb2 = tb2.getTypeArguments()[0];
    }

    var result = aAllTypePairsResolver.resolve(javaElement1, javaElement2).parallelStream()
        .filter(p -> p.getValue0().getPackageFragment().equals(p.getValue1().getPackageFragment()))
        .filter(p -> areTokensOk(p))
        .collect(Collectors.toSet());


    cache.put(Pair.with(javaElement1, javaElement2), Optional.of(result));
    return Optional.of(result);
  }

  private final boolean areTokensOk(Pair<IType, IType> sub) {
    var s1Tokens = TokensUtil.splitCamelCase(sub.getValue0().getElementName());
    var s2Tokens = TokensUtil.splitCamelCase(sub.getValue1().getElementName());


    if (Math.abs(s1Tokens.size() - s2Tokens.size()) > TOKENS_MAX_DIFF) {
      return false;
    }

    var avgNameLength = (s1Tokens.size() + s2Tokens.size()) * 1.0 / 2.0;

    s1Tokens.retainAll(s2Tokens);


    return (s1Tokens.size()) / avgNameLength >= TOKENS_THRESHOLD;

  };

}

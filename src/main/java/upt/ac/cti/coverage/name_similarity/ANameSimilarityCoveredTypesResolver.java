package upt.ac.cti.coverage.name_similarity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;
import upt.ac.cti.aperture.AAllTypePairsResolver;
import upt.ac.cti.config.Config;
import upt.ac.cti.coverage.ICoveredTypesResolver;
import upt.ac.cti.util.binding.ABindingResolver;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.cache.CacheRegion;
import upt.ac.cti.util.computation.TokensUtil;

abstract class ANameSimilarityCoveredTypesResolver<J extends IJavaElement>
    implements ICoveredTypesResolver<J> {

  protected final ABindingResolver<J, ITypeBinding> aBindingResolver;
  protected final AAllTypePairsResolver<J> aAllTypePairsResolver;

  private final Cache<Pair<J, J>, Optional<Set<Pair<IType, IType>>>> cache;

  public ANameSimilarityCoveredTypesResolver(
      ABindingResolver<J, ITypeBinding> aBindingResolver,
      AAllTypePairsResolver<J> aAllTypePairsResolver,
      CacheRegion region) {
    this.aBindingResolver = aBindingResolver;
    this.aAllTypePairsResolver = aAllTypePairsResolver;
    cache = new Cache<>(region);
  }

  protected abstract Optional<Set<Pair<IType, IType>>> computeResult(J javaElement1,
      J javaElement2);

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

    if (typeBinding1O.get() == null
        || !(typeBinding1O.get().getJavaElement() instanceof IType)
        || typeBinding2O.get() == null
        || !(typeBinding2O.get().getJavaElement() instanceof IType)) {
      return Optional.empty();
    }

    var result = computeResult(javaElement1, javaElement2);

    cache.put(Pair.with(javaElement1, javaElement2), result);
    return result;
  }

  protected Pair<List<String>, List<String>> rootTokens(J javaElement1, J javaElement2) {
    var typeBinding1O = aBindingResolver.resolve(javaElement1);
    var typeBinding2O = aBindingResolver.resolve(javaElement2);

    var root1 = (IType) typeBinding1O.get().getJavaElement();
    var root2 = (IType) typeBinding2O.get().getJavaElement();

    var r1Tokens = TokensUtil.splitCamelCase(root1.getElementName());
    var r2Tokens = TokensUtil.splitCamelCase(root2.getElementName());

    return Pair.with(r1Tokens, r2Tokens);
  }

  protected boolean validateTokens(Pair<IType, IType> sub,
      Pair<List<String>, List<String>> hierarchyRootsTokens) {
    var root1Tokens = hierarchyRootsTokens.getValue0();
    var root2Tokens = hierarchyRootsTokens.getValue1();

    var commonRoot = new ArrayList<>(root1Tokens);
    commonRoot.removeAll(root2Tokens);

    var s1Tokens = TokensUtil.splitCamelCase(sub.getValue0().getElementName());
    var s2Tokens = TokensUtil.splitCamelCase(sub.getValue1().getElementName());

    s1Tokens.removeAll(commonRoot);
    s2Tokens.removeAll(commonRoot);


    if (Math.abs(s1Tokens.size() - s2Tokens.size()) > Config.TOKENS_MAX_DIFF) {
      return false;
    }

    var avgNameLength = (s1Tokens.size() + s2Tokens.size()) * 1.0 / 2.0;

    s1Tokens.retainAll(s2Tokens);


    return (s1Tokens.size()) / avgNameLength >= Config.TOKENS_THRESHOLD;

  };


}

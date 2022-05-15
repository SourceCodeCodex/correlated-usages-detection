package upt.ac.cti.aperture;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;
import upt.ac.cti.util.binding.ABindingResolver;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.cache.CacheRegions;
import upt.ac.cti.util.computation.CartesianProduct;
import upt.ac.cti.util.hierarchy.HierarchyResolver;
import upt.ac.cti.util.validation.IsTypeBindingCollection;

public abstract class AAllTypePairsResolver<J extends IJavaElement> {
  private final HierarchyResolver hierarchyResolver;
  private final ABindingResolver<J, ITypeBinding> aBindingResolver;
  private final Cache<J, List<IType>> cache = new Cache<>(CacheRegions.ALL_TYPES);

  public AAllTypePairsResolver(ABindingResolver<J, ITypeBinding> aBindingResolver,
      HierarchyResolver hierarchyResolver) {
    this.hierarchyResolver = hierarchyResolver;
    this.aBindingResolver = aBindingResolver;
  }

  public Set<Pair<IType, IType>> resolve(J javaElement1, J javaElement2) {

    var cached1 = cache.get(javaElement1);
    var cached2 = cache.get(javaElement2);

    var types1 = (cached1.isPresent() ? cached1.get() : resolve(javaElement1)).parallelStream()
        .filter(this::isNotAnonymous).toList();

    if (cached1.isEmpty()) {
      cache.put(javaElement1, types1);
    }

    var types2 =
        (cached2.isPresent() ? cached2.get() : resolve(javaElement2)).parallelStream()
            .filter(this::isNotAnonymous).toList();

    if (cached2.isEmpty()) {
      cache.put(javaElement2, types2);
    }

    var result = new HashSet<>(CartesianProduct.product(types1, types2));
    return result;
  }

  public List<IType> resolve(J javaElement) {
    var typeBindingOpt = aBindingResolver.resolve(javaElement);
    if (typeBindingOpt.isEmpty()) {
      return List.of();
    }
    var typeBinding = typeBindingOpt.get();

    var isCollection = new IsTypeBindingCollection();

    IJavaElement type;
    if (isCollection.test(typeBinding)) {
      type = typeBinding.getTypeArguments()[0].getJavaElement();
    } else {
      type = typeBinding.getJavaElement();
    }

    if (type == null || !(type instanceof IType)) {
      return List.of();
    }

    return hierarchyResolver.resolveConcrete((IType) type);
  }

  private boolean isNotAnonymous(IType type) {
    try {
      return !type.isAnonymous();
    } catch (JavaModelException e) {
      e.printStackTrace();
      return false;
    }
  }
}

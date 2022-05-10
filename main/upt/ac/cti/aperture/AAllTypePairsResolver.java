package upt.ac.cti.aperture;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;
import upt.ac.cti.util.CartesianProduct;
import upt.ac.cti.util.binding.ABindingResolver;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.hierarchy.HierarchyResolver;
import upt.ac.cti.util.validation.IsTypeBindingCollection;

public abstract class AAllTypePairsResolver<J extends IJavaElement> {
  private final HierarchyResolver hierarchyResolver;
  private final ABindingResolver<J, ITypeBinding> aBindingResolver;
  private final Cache<Pair<J, J>, Set<Pair<IType, IType>>> cache = new Cache<>();

  public AAllTypePairsResolver(ABindingResolver<J, ITypeBinding> aBindingResolver,
      HierarchyResolver hierarchyResolver) {
    this.hierarchyResolver = hierarchyResolver;
    this.aBindingResolver = aBindingResolver;
  }

  public Set<Pair<IType, IType>> resolve(J javaElement1, J javaElement2) {
    var cached = cache.get(Pair.with(javaElement1, javaElement2));
    if (cached.isPresent()) {
      return cached.get();
    }

    var types1 = resolve(javaElement1).stream().filter(t -> {
      try {
        return !t.isAnonymous();
      } catch (JavaModelException e) {
        e.printStackTrace();
        return false;
      }
    }).toList();

    var types2 = resolve(javaElement2).stream().filter(t -> {
      try {
        return !t.isAnonymous();
      } catch (JavaModelException e) {
        e.printStackTrace();
        return false;
      }
    }).toList();;

    var result = new HashSet<>(CartesianProduct.product(types1, types2));


    cache.put(Pair.with(javaElement1, javaElement2), result);
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
}

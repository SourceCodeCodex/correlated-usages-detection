package upt.ac.cti.aperture;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;
import upt.ac.cti.util.binding.ABindingResolver;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;
import upt.ac.cti.util.validation.IsTypeBindingCollection;

abstract class AAllTypePairsResolver<J extends IJavaElement> {
  private final ConcreteDescendantsResolver hierarchyResolver;
  private final ABindingResolver<J, ITypeBinding> aBindingResolver;

  public AAllTypePairsResolver(ABindingResolver<J, ITypeBinding> aBindingResolver,

      ConcreteDescendantsResolver hierarchyResolver) {
    this.aBindingResolver = aBindingResolver;
    this.hierarchyResolver = hierarchyResolver;
  }

  protected Set<Pair<IType, IType>> resolve(J javaElement1, J javaElement2) {
    var types1 = concreteTypes(javaElement1);
    var types2 = concreteTypes(javaElement2);

    return new HashSet<>(product(types1, types2));
  }


  private List<IType> concreteTypes(J javaElement) {
    var typeBindingOpt = aBindingResolver.resolve(javaElement);
    if (typeBindingOpt.isEmpty()) {
      return List.of();
    }
    var typeBinding = typeBindingOpt.get();

    var isCollection = new IsTypeBindingCollection();

    IType type;
    if (isCollection.test(typeBinding)) {
      type = (IType) typeBinding.getTypeArguments()[0].getJavaElement();
    } else {
      type = (IType) typeBinding.getJavaElement();
    }

    if (type == null) {
      return List.of();
    }


    return hierarchyResolver.resolve(type);
  }

  private <T> List<Pair<T, T>> product(List<T> s1, List<T> s2) {
    return s1.stream().flatMap(it -> s2.stream().map(el -> Pair.with(it, el))).toList();
  }


}

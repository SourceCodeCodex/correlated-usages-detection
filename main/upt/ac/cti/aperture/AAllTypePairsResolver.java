package upt.ac.cti.aperture;

import java.util.Collection;
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

public abstract class AAllTypePairsResolver<J extends IJavaElement> {
  private final ConcreteDescendantsResolver concreteDescendantsResolver;
  private final ABindingResolver<J, ITypeBinding> aBindingResolver;

  public AAllTypePairsResolver(ABindingResolver<J, ITypeBinding> aBindingResolver,
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    this.concreteDescendantsResolver = concreteDescendantsResolver;
    this.aBindingResolver = aBindingResolver;
  }

  public Set<Pair<IType, IType>> resolve(J javaElement1, J javaElement2) {
    var types1 = resolve(javaElement1);
    var types2 = resolve(javaElement2);

    return new HashSet<>(product(types1, types2));
  }


  public List<IType> resolve(J javaElement) {
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


    return concreteDescendantsResolver.resolve(type);
  }

  public static <T, C extends Collection<T>> List<Pair<T, T>> product(C s1, C s2) {
    return s1.stream().flatMap(it -> s2.stream().map(el -> Pair.with(it, el))).toList();
  }
}

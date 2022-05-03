package upt.ac.cti.util.validation;

import java.util.function.Predicate;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import upt.ac.cti.util.binding.ABindingResolver;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;

class VariableValidator<J extends IJavaElement> implements Predicate<J> {

  private final ConcreteDescendantsResolver concreteDescendantsResolver;
  private final ABindingResolver<J, ITypeBinding> aBindingResolver;

  public VariableValidator(ABindingResolver<J, ITypeBinding> aBindingResolver,
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    this.aBindingResolver = aBindingResolver;
    this.concreteDescendantsResolver = concreteDescendantsResolver;
  }

  @Override
  public boolean test(J t) {
    var typeBinding = aBindingResolver.resolve(t);
    if (typeBinding.isEmpty()) {
      return false;
    }
    return testTypeBinding(typeBinding.get());
  }

  public boolean testTypeBinding(ITypeBinding binding) {
    return !binding.isPrimitive() &&
        !binding.isArray() &&
        !binding.isSynthetic() &&
        binding.isFromSource() &&
        hasTypeDescendants(binding) &&
        ifCollectionAndValid(binding);
  }

  private final boolean hasTypeDescendants(ITypeBinding binding) {
    var javaType = (IType) binding.getJavaElement();
    if (javaType == null) {
      return false;
    }
    return concreteDescendantsResolver.resolve(javaType)
        .size() >= 2;
  }

  private final boolean ifCollectionAndValid(ITypeBinding binding) {
    if (new IsTypeBindingCollection().test(binding)) {
      var typeParamBinding = binding.getTypeArguments()[0];
      return testTypeBinding(typeParamBinding);
    }
    return true;
  }


}

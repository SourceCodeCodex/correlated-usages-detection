package upt.ac.cti.util.validation;

import java.util.function.Predicate;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import upt.ac.cti.config.Config;
import upt.ac.cti.util.binding.ParameterTypeBindingResolver;
import upt.ac.cti.util.hierarchy.HierarchyResolver;

public final class ParameterValidator implements Predicate<ILocalVariable> {

  private final ParameterTypeBindingResolver parameterTypeBindingResolver;
  private final HierarchyResolver hierarchyResolver;

  public ParameterValidator(ParameterTypeBindingResolver parameterTypeBindingResolver,
      HierarchyResolver hierarchyResolver) {
    this.parameterTypeBindingResolver = parameterTypeBindingResolver;
    this.hierarchyResolver = hierarchyResolver;
  }

  @Override
  public boolean test(ILocalVariable t) {
    var typeBinding = parameterTypeBindingResolver.resolve(t);
    if (typeBinding.isEmpty()) {
      return false;
    }
    return testInternal(typeBinding.get());
  }

  private boolean testInternal(ITypeBinding binding) {
    return !binding.isPrimitive() &&
        !binding.isArray() &&
        !binding.isSynthetic() &&
        binding.isFromSource() &&
        hasTypeDescendants(binding) &&
        isNotCollection(binding);
  }


  private boolean hasTypeDescendants(ITypeBinding binding) {
    var javaType = binding.getJavaElement();
    if (javaType == null || !(javaType instanceof IType)) {
      return false;
    }
    return hierarchyResolver.resolveConcrete((IType) javaType)
        .size() >= Config.MIN_HIERARCHY_SIZE;
  }

  private boolean isNotCollection(ITypeBinding binding) {
    return !new IsTypeBindingCollection().test(binding);
  }


}


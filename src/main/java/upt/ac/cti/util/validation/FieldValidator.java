package upt.ac.cti.util.validation;

import java.util.function.Predicate;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;
import upt.ac.cti.config.Config;
import upt.ac.cti.util.binding.FieldTypeBindingResolver;
import upt.ac.cti.util.hierarchy.HierarchyResolver;

public final class FieldValidator implements Predicate<IField> {

  private final FieldTypeBindingResolver fieldTypeBindingResolver;
  private final HierarchyResolver hierarchyResolver;

  public FieldValidator(FieldTypeBindingResolver fieldTypeBindingResolver,
      HierarchyResolver hierarchyResolver) {
    this.fieldTypeBindingResolver = fieldTypeBindingResolver;
    this.hierarchyResolver = hierarchyResolver;
  }

  @Override
  public boolean test(IField t) {
    var typeBinding = fieldTypeBindingResolver.resolve(t);
    if (typeBinding.isEmpty()) {
      return false;
    }
    return isNotStatic(t) && testInternal(typeBinding.get());
  }

  private boolean testInternal(ITypeBinding binding) {
    return ifCollectionAndValid(binding) || testInternal1(binding);
  }

  private boolean testInternal1(ITypeBinding binding) {
    return !binding.isPrimitive() &&
        !binding.isArray() &&
        !binding.isSynthetic() &&
        binding.isFromSource() &&
        hasTypeDescendants(binding);
  }

  private final boolean isNotStatic(IField field) {
    try {
      return !Flags.isStatic(field.getFlags());
    } catch (JavaModelException e) {
      e.printStackTrace();

      return false;
    }
  }

  private boolean hasTypeDescendants(ITypeBinding binding) {
    var javaType = binding.getJavaElement();
    if (javaType == null || !(javaType instanceof IType)) {
      return false;
    }
    return hierarchyResolver.resolveConcrete((IType) javaType)
        .size() >= Config.MIN_HIERARCHY_SIZE;
  }

  private boolean ifCollectionAndValid(ITypeBinding binding) {
    if (new IsTypeBindingCollection().test(binding)) {
      var typeParamBinding = binding.getTypeArguments()[0];
      return testInternal1(typeParamBinding);
    }
    return false;
  }
}


package upt.ac.cti.util.validation;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import upt.ac.cti.util.binding.FieldTypeBindingResolver;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;

public final class FieldValidator extends VariableValidator<IField> {

  public FieldValidator(FieldTypeBindingResolver fieldTypeBindingResolver,
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    super(fieldTypeBindingResolver, concreteDescendantsResolver);
  }

  @Override
  public boolean test(IField field) {
    return isNotStatic(field) && super.test(field);
  }

  private final boolean isNotStatic(IField field) {
    try {
      return !Flags.isStatic(field.getFlags());
    } catch (JavaModelException e) {
      e.printStackTrace();

      return false;
    }
  }

}


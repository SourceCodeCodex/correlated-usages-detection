package upt.ac.cti.util.validation;

import org.eclipse.jdt.core.ILocalVariable;
import upt.ac.cti.util.binding.ParameterTypeBindingResolver;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;

public final class ParameterValidator extends VariableValidator<ILocalVariable> {

  public ParameterValidator(ParameterTypeBindingResolver parameterTypeBindingResolver,
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    super(parameterTypeBindingResolver, concreteDescendantsResolver);
  }

  @Override
  public boolean test(ILocalVariable parameter) {
    return super.test(parameter);
  }


}


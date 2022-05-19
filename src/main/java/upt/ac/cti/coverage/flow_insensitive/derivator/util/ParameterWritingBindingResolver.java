package upt.ac.cti.coverage.flow_insensitive.derivator.util;

import org.eclipse.jdt.core.ILocalVariable;
import upt.ac.cti.dependency.Dependencies;

public class ParameterWritingBindingResolver extends ADerivableWritingBindingResolver<ILocalVariable> {

  public ParameterWritingBindingResolver() {
    super(Dependencies.parameterTypeBindingResolver);
  }

}

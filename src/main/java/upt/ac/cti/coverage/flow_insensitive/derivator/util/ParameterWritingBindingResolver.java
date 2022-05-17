package upt.ac.cti.coverage.flow_insensitive.derivator.util;

import org.eclipse.jdt.core.ILocalVariable;
import upt.ac.cti.dependency.Dependencies;

public class ParameterWritingBindingResolver extends AWritingBindingResolver<ILocalVariable> {

  public ParameterWritingBindingResolver() {
    super(Dependencies.parameterTypeBindingResolver);
  }

}

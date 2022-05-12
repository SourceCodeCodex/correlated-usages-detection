package upt.ac.cti.coverage.derivator.util;

import org.eclipse.jdt.core.ILocalVariable;
import upt.ac.cti.util.binding.ParameterTypeBindingResolver;
import upt.ac.cti.util.hierarchy.HierarchyResolver;

public class ParameterWritingBindingResolver extends AWritingBindingResolver<ILocalVariable> {

  public ParameterWritingBindingResolver(HierarchyResolver hierarchyResolver,
      ParameterTypeBindingResolver parameterTypeBindingResolver) {
    super(hierarchyResolver, parameterTypeBindingResolver);
  }

}

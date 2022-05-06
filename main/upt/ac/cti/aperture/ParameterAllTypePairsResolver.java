package upt.ac.cti.aperture;

import java.util.Set;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.util.binding.ParameterTypeBindingResolver;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;

public class ParameterAllTypePairsResolver extends AAllTypePairsResolver<ILocalVariable> {

  public ParameterAllTypePairsResolver(ParameterTypeBindingResolver parameterTypeBindingResolver,
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    super(parameterTypeBindingResolver, concreteDescendantsResolver);
  }

  @Override
  public Set<Pair<IType, IType>> resolve(ILocalVariable param1, ILocalVariable param2) {
    return super.resolve(param1, param2);
  }


}

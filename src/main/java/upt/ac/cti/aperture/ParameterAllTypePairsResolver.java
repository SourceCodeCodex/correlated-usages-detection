package upt.ac.cti.aperture;

import java.util.Set;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;
import upt.ac.cti.util.binding.ABindingResolver;

public class ParameterAllTypePairsResolver extends AAllTypePairsResolver<ILocalVariable> {

  public ParameterAllTypePairsResolver(
      ABindingResolver<ILocalVariable, ITypeBinding> aBindingResolver) {
    super(aBindingResolver);
  }

  @Override
  public Set<Pair<IType, IType>> resolve(ILocalVariable param1, ILocalVariable param2) {
    return super.resolve(param1, param2);
  }


}

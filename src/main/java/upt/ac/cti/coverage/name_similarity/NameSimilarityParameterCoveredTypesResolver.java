package upt.ac.cti.coverage.name_similarity;

import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.aperture.ParameterAllTypePairsResolver;
import upt.ac.cti.util.binding.ParameterTypeBindingResolver;

public final class NameSimilarityParameterCoveredTypesResolver
    extends ANameSimilarityCoveredTypesResolver<ILocalVariable> {

  public NameSimilarityParameterCoveredTypesResolver(
      ParameterAllTypePairsResolver parameterAllTypePairsResolver,
      ParameterTypeBindingResolver parameterTypeBindingResolver) {
    super(parameterTypeBindingResolver,
        parameterAllTypePairsResolver);
  }

  @Override
  public Optional<Set<Pair<IType, IType>>> resolve(ILocalVariable param1, ILocalVariable param2) {
    return super.resolve(param1, param2);
  }

}

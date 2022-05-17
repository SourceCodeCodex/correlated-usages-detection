package upt.ac.cti.coverage.name_similarity;

import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.dependency.Dependencies;

public final class SquanderingNameSimilarityParameterCoveredTypesResolver
    extends ASquanderingNameSimilarityCoveredTypesResolver<ILocalVariable> {

  public SquanderingNameSimilarityParameterCoveredTypesResolver() {
    super(Dependencies.parameterTypeBindingResolver,
        Dependencies.parameterAllTypePairsResolver);
  }

  @Override
  public Optional<Set<Pair<IType, IType>>> resolve(ILocalVariable param1, ILocalVariable param2) {
    return super.resolve(param1, param2);
  }

}

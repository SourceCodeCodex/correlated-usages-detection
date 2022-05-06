package upt.ac.cti.coverage;

import static upt.ac.cti.dependencies.DependencyUtils.newParameterAllTypePairsResolver;
import java.util.Set;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.coverage.combiner.parameter.ParameterWritingsCombiner;
import upt.ac.cti.util.binding.ParameterTypeBindingResolver;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public final class ParameterCoveredTypesResolver extends ACoveredTypesResolver<ILocalVariable> {

  public ParameterCoveredTypesResolver(CodeParser codeParser,
      JavaEntitySearcher javaEntitySearcher,
      ParameterTypeBindingResolver parameterTypeBindingResolver,
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    super(new ParameterWritingsCombiner(codeParser, javaEntitySearcher),
        codeParser,
        javaEntitySearcher,
        parameterTypeBindingResolver,
        newParameterAllTypePairsResolver(parameterTypeBindingResolver,
            concreteDescendantsResolver));
  }

  @Override
  public Set<Pair<IType, IType>> resolve(ILocalVariable param1, ILocalVariable param2) {
    return super.resolve(param1, param2);
  }

}

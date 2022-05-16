package upt.ac.cti.coverage.flow_insensitive;

import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.aperture.ParameterAllTypePairsResolver;
import upt.ac.cti.coverage.flow_insensitive.combiner.parameter.ParameterWritingsCombiner;
import upt.ac.cti.coverage.flow_insensitive.derivator.util.ParameterWritingBindingResolver;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public final class FlowInsensitiveParameterCoveredTypesResolver extends AFlowInsensitiveCoveredTypesResolver<ILocalVariable> {

  public FlowInsensitiveParameterCoveredTypesResolver(
      CodeParser codeParser,
      JavaEntitySearcher javaEntitySearcher,
      ParameterWritingBindingResolver parameterWritingBindingResolver,
      ParameterAllTypePairsResolver parameterAllTypePairsResolver) {
    super(new ParameterWritingsCombiner(codeParser, javaEntitySearcher),
        codeParser,
        javaEntitySearcher,
        parameterAllTypePairsResolver,
        parameterWritingBindingResolver);
  }

  @Override
  public Optional<Set<Pair<IType, IType>>> resolve(ILocalVariable param1, ILocalVariable param2) {
    return super.resolve(param1, param2);
  }

}

package upt.ac.cti.coverage;

import java.util.Set;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.coverage.combiner.parameter.ParameterWritingsCombiner;
import upt.ac.cti.coverage.parsing.CodeParser;
import upt.ac.cti.coverage.search.JavaEntitySearcher;

public final class ParameterCoveredTypesResolver extends ACoveredTypesResolver<ILocalVariable> {

  public ParameterCoveredTypesResolver(CodeParser codeParser,
      JavaEntitySearcher javaEntitySearcher) {
    super(new ParameterWritingsCombiner(codeParser, javaEntitySearcher), codeParser,
        javaEntitySearcher);
  }

  @Override
  public Set<Pair<IType, IType>> resolve(ILocalVariable param1, ILocalVariable param2) {
    return super.resolve(param1, param2);
  }

}

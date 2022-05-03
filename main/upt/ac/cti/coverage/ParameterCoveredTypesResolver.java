package upt.ac.cti.coverage;

import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.coverage.combiner.parameter.ParameterWritingsCombiner;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.parsing.CodeParser;
import upt.ac.cti.coverage.search.JavaEntitySearcher;

public final class ParameterCoveredTypesResolver extends ACoveredTypesResolver {

  public ParameterCoveredTypesResolver(CodeParser codeParser,
      JavaEntitySearcher javaEntitySearcher) {
    super(codeParser, javaEntitySearcher);
  }

  public Set<Pair<IType, IType>> resolve(ILocalVariable param1, ILocalVariable param2) {
    return super.resolve(param1, param2);
  }

  @Override
  protected List<Pair<Writing, Writing>> writingPairs(IJavaElement javaElement1,
      IJavaElement javaElement2) {
    var combiner = new ParameterWritingsCombiner(codeParser, javaEntitySearcher);

    return combiner.combine((ILocalVariable) javaElement1, (ILocalVariable) javaElement2);
  }

}

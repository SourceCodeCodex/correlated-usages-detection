package upt.ac.cti.coverage.combiner.parameter;

import java.util.List;
import org.eclipse.jdt.core.ILocalVariable;
import org.javatuples.Pair;
import upt.ac.cti.coverage.combiner.IWritingsCombiner;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.parsing.CodeParser;
import upt.ac.cti.coverage.search.JavaEntitySearcher;
import upt.ac.cti.util.binding.ParameterTypeBindingResolver;
import upt.ac.cti.util.validation.IsJavaElementCollection;

public class ParameterWritingsCombiner implements IWritingsCombiner<ILocalVariable> {

  private final CodeParser parser;
  private final JavaEntitySearcher javaEntitySearcher;
  private final IsJavaElementCollection<ILocalVariable> isCollection;

  public ParameterWritingsCombiner(CodeParser parser, JavaEntitySearcher javaEntitySearcher) {
    this.parser = parser;
    this.javaEntitySearcher = javaEntitySearcher;
    this.isCollection =
        new IsJavaElementCollection<>(new ParameterTypeBindingResolver());
  }


  @Override
  public List<Pair<Writing, Writing>> combine(ILocalVariable param1,
      ILocalVariable param2) {
    return List.of();
  }
}

package upt.ac.cti.coverage.flow_insensitive.combiner.parameter;

import java.util.List;
import java.util.stream.Stream;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.javatuples.Pair;
import upt.ac.cti.coverage.flow_insensitive.combiner.IWritingsCombiner;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public class ParameterWritingsCombiner implements IWritingsCombiner<ILocalVariable> {

  private final CodeParser codeParser = Dependencies.codeParser;
  private final JavaEntitySearcher javaEntitySearcher = Dependencies.javaEntitySearcher;

  @Override
  public List<Pair<Writing<ILocalVariable>, Writing<ILocalVariable>>> combine(ILocalVariable param1,
      ILocalVariable param2) {

    if (!param1.getDeclaringMember().equals(param2.getDeclaringMember())) {
      return List.of();
    }

    var invocations =
        javaEntitySearcher.searchMethodInvocations((IMethod) param1.getDeclaringMember());

    var newWritingPairs = invocations.stream().flatMap(it -> {
      var visitor = new BothArgumentsInvocationVisitor(param1, param2);
      if (it.getCompilationUnit() == null) {
        return Stream.empty();
      }
      var nodeOpt = codeParser.parse(it.getCompilationUnit());

      return nodeOpt.map(node -> {
        node.accept(visitor);
        return visitor.newPairings().stream();
      }).orElse(Stream.empty());
    }).toList();

    return newWritingPairs;
  }
}

package upt.ac.cti.coverage.derivator.internal;

import java.util.stream.Stream;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.Name;
import org.javatuples.Pair;
import upt.ac.cti.coverage.derivator.internal.visitors.ArgumentMethodInvocationVisitor;
import upt.ac.cti.coverage.model.DerivationResult;
import upt.ac.cti.coverage.model.NewWritingPairs;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.parsing.CodeParser;
import upt.ac.cti.coverage.search.JavaEntitySearcher;

final public class NameParameterDerivator implements IFieldWritingsDerivator {

  private final JavaEntitySearcher javaEntitySearcher;
  private final CodeParser codeParser;

  public NameParameterDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.javaEntitySearcher = javaEntitySearcher;
    this.codeParser = codeParser;
  }

  @Override
  public DerivationResult derive(Writing deriver, Writing constant) {
    var name = (Name) deriver.writingExpression();
    var binding = name.resolveBinding();

    if (binding == null) {
      return NewWritingPairs.NULL;
    }

    var parameter =
        (ILocalVariable) binding.getJavaElement();

    if (parameter == null) {
      return NewWritingPairs.NULL;
    }

    var invocations =
        javaEntitySearcher.searchMethodInvocations((IMethod) parameter.getDeclaringMember());

    var derivations = invocations.stream().flatMap(it -> {
      var visitor = new ArgumentMethodInvocationVisitor(deriver, parameter);
      if (it.getCompilationUnit() == null) {
        return Stream.empty();
      }
      var nodeOpt = codeParser.parse(it.getCompilationUnit());

      return nodeOpt.map(node -> {
        node.accept(visitor);
        return visitor.derivations().stream();
      }).orElse(Stream.empty());
    });

    var newWritingPairs = derivations
        .map(derivation -> Pair.with(derivation, constant))
        .toList();

    return new NewWritingPairs(newWritingPairs);
  }

}

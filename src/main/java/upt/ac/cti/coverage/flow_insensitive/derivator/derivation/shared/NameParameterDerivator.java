package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared;

import java.util.stream.Stream;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.Name;
import org.javatuples.Pair;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.IWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.visitor.ArgumentMethodInvocationVisitor;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

final public class NameParameterDerivator<J extends IJavaElement> implements IWritingsDerivator<J> {

  private final JavaEntitySearcher javaEntitySearcher;
  private final CodeParser codeParser;

  public NameParameterDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.javaEntitySearcher = javaEntitySearcher;
    this.codeParser = codeParser;
  }

  @Override
  public NewWritingPairs<J> derive(Writing<J> deriver, Writing<J> constant) {
    var name = (Name) deriver.writingExpression();
    var binding = name.resolveBinding();

    if (binding == null) {
      return NewWritingPairs.NULL();
    }

    var parameter =
        (ILocalVariable) binding.getJavaElement();

    if (parameter == null) {
      return NewWritingPairs.NULL();
    }

    var invocations =
        javaEntitySearcher.searchMethodInvocations((IMethod) parameter.getDeclaringMember());

    var derivations = invocations.stream().flatMap(it -> {
      var visitor = new ArgumentMethodInvocationVisitor<>(deriver, parameter);
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
        .map(Writing::increaseDepth)
        .map(derivation -> Pair.with(derivation, constant))
        .toList();

    return new NewWritingPairs<>(newWritingPairs);
  }

}

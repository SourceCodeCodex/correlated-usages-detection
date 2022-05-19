package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.simple;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.Name;
import org.javatuples.Pair;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.ISimpleWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.simple.visitor.LocalVariableAssignmentVisitor;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

final class NameLocalVariableDerivator<J extends IJavaElement>
    implements ISimpleWritingsDerivator<J> {

  private final JavaEntitySearcher javaEntitySearcher = Dependencies.javaEntitySearcher;
  private final CodeParser codeParser = Dependencies.codeParser;


  @Override
  public NewWritingPairs<J> derive(DerivableWriting<J> deriver, Writing<J> constant) {
    var name = (Name) deriver.writingExpression();
    var binding = name.resolveBinding();

    if (binding == null) {
      return NewWritingPairs.NULL();
    }

    var localVariable =
        (ILocalVariable) binding.getJavaElement();

    if (localVariable == null) {
      return NewWritingPairs.NULL();
    }

    var writingMethods = javaEntitySearcher.searchLocalVariableWritings(localVariable);

    List<? extends Writing<J>> derivations = writingMethods.stream()
        .map(method -> codeParser.parse(method))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .flatMap(node -> {
          var visitor = new LocalVariableAssignmentVisitor<>(deriver, localVariable);
          node.accept(visitor);
          return visitor.derivations().stream();
        }).toList();

    var newWritingPairs =
        derivations.stream()
            .map(derivation -> Pair.with(derivation, constant))
            .toList();

    if (newWritingPairs.isEmpty()) {
      return NewWritingPairs.of(Pair.with(deriver.toUnderivableWriting(), constant));
    }

    return new NewWritingPairs<>(newWritingPairs);
  }

}

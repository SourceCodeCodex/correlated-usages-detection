package upt.ac.cti.coverage.derivator.derivation.simple;

import java.util.Optional;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.Name;
import org.javatuples.Pair;
import upt.ac.cti.coverage.derivator.derivation.IWritingsDerivator;
import upt.ac.cti.coverage.derivator.derivation.simple.visitor.LocalVariableAssignmentVisitor;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

final class NameLocalVariableDerivator<J extends IJavaElement>
    implements IWritingsDerivator<J> {

  private final JavaEntitySearcher javaEntitySearcher;
  private final CodeParser codeParser;

  public NameLocalVariableDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
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

    var localVariable =
        (ILocalVariable) binding.getJavaElement();

    if (localVariable == null) {
      return NewWritingPairs.NULL();
    }

    var writingMethods = javaEntitySearcher.searchLocalVariableWritings(localVariable);

    var derivations = writingMethods.stream()
        .map(method -> codeParser.parse(method))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .flatMap(node -> {
          var visitor = new LocalVariableAssignmentVisitor<>(deriver, localVariable);
          node.accept(visitor);
          return visitor.derivations().stream();
        }).toList();

    var newWritingPairs = derivations.stream()
        .map(derivation -> Pair.with(derivation, constant))
        .toList();;

    return new NewWritingPairs<>(newWritingPairs);
  }

}

package upt.ac.cti.coverage.derivator.internal;

import java.util.Optional;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.Name;
import org.javatuples.Pair;
import upt.ac.cti.coverage.derivator.internal.visitors.LocalVariableAssignmentVisitor;
import upt.ac.cti.coverage.model.DerivationResult;
import upt.ac.cti.coverage.model.NewWritingPairs;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.parsing.CodeParser;
import upt.ac.cti.coverage.search.JavaEntitySearcher;

final class NameLocalVariableDerivator implements IFieldWritingsDerivator {

  private final JavaEntitySearcher javaEntitySearcher;
  private final CodeParser codeParser;

  public NameLocalVariableDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
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

    var localVariable =
        (ILocalVariable) binding.getJavaElement();

    if (localVariable == null) {
      return NewWritingPairs.NULL;
    }

    var writingMethods = javaEntitySearcher.searchLocalVariableWritings(localVariable);

    var derivations = writingMethods.stream()
        .map(method -> codeParser.parse(method))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .flatMap(methodDeclaration -> {
          var visitor = new LocalVariableAssignmentVisitor(deriver, localVariable);
          var node = methodDeclaration.getBody();
          node.accept(visitor);
          return visitor.derivations().stream();
        }).toList();

    var newWritingPairs = derivations.stream()
        .map(derivation -> Pair.with(derivation, constant))
        .toList();;

    return new NewWritingPairs(newWritingPairs);
  }

}

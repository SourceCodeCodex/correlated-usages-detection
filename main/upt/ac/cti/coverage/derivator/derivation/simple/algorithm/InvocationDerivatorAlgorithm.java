package upt.ac.cti.coverage.derivator.derivation.simple.algorithm;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.javatuples.Pair;
import upt.ac.cti.coverage.derivator.derivation.simple.visitors.MethodReturnVisitor;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;
import upt.ac.cti.util.parsing.CodeParser;

public class InvocationDerivatorAlgorithm<J extends IJavaElement> {

  private final CodeParser codeParser;

  public InvocationDerivatorAlgorithm(CodeParser codeParser) {
    this.codeParser = codeParser;
  }

  public NewWritingPairs<J> derive(Writing<J> deriver, Writing<J> constant, IMethod method) {
    var node = codeParser.parse(method);
    if (node.isEmpty()) {
      return NewWritingPairs.NULL();
    }
    var visitor = new MethodReturnVisitor<>(deriver);
    node.get().accept(visitor);

    var newWritingPairs = visitor.derivations().stream()
        .map(derivation -> Pair.with(derivation, constant))
        .toList();;

    return new NewWritingPairs<>(newWritingPairs);
  }
}

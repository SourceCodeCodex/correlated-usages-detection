package upt.ac.cti.coverage.derivator.internal.algorithm;

import org.eclipse.jdt.core.IMethod;
import org.javatuples.Pair;
import upt.ac.cti.coverage.derivator.internal.visitors.MethodReturnVisitor;
import upt.ac.cti.coverage.model.DerivationResult;
import upt.ac.cti.coverage.model.NewWritingPairs;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.parsing.CodeParser;

public class InvocationDerivatorAlgorithm {

  private final CodeParser codeParser;

  public InvocationDerivatorAlgorithm(CodeParser codeParser) {
    this.codeParser = codeParser;
  }

  public DerivationResult derive(Writing deriver, Writing constant, IMethod method) {
    var node = codeParser.parse(method);
    if (node.isEmpty()) {
      return NewWritingPairs.NULL;
    }
    var visitor = new MethodReturnVisitor(deriver);
    node.get().accept(visitor);

    var newWritingPairs = visitor.derivations().stream()
        .map(derivation -> Pair.with(derivation, constant))
        .toList();;

    return new NewWritingPairs(newWritingPairs);
  }
}

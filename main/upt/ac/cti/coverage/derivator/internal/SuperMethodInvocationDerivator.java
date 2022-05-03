package upt.ac.cti.coverage.derivator.internal;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import upt.ac.cti.coverage.derivator.internal.algorithm.InvocationDerivatorAlgorithm;
import upt.ac.cti.coverage.model.DerivationResult;
import upt.ac.cti.coverage.model.NewWritingPairs;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.parsing.CodeParser;

final class SuperMethodInvocationDerivator implements IFieldWritingsDerivator {

  private final InvocationDerivatorAlgorithm algorithm;

  public SuperMethodInvocationDerivator(CodeParser codeParser) {
    this.algorithm = new InvocationDerivatorAlgorithm(codeParser);
  }

  @Override
  public DerivationResult derive(Writing deriver, Writing constant) {
    var methodInvocation = (SuperMethodInvocation) deriver.writingExpression();
    var binding = methodInvocation.resolveMethodBinding();
    if (binding == null) {
      return NewWritingPairs.NULL;
    }

    var method = (IMethod) binding.getJavaElement();
    if (method == null) {
      return NewWritingPairs.NULL;
    }


    return algorithm.derive(deriver, constant, method);
  }
}



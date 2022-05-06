package upt.ac.cti.coverage.derivator.derivation.simple;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.MethodInvocation;
import upt.ac.cti.coverage.derivator.derivation.IWritingsDerivator;
import upt.ac.cti.coverage.derivator.derivation.simple.algorithm.InvocationDerivatorAlgorithm;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;
import upt.ac.cti.util.parsing.CodeParser;

final class MethodInvocationDerivator<J extends IJavaElement> implements IWritingsDerivator<J> {

  private final InvocationDerivatorAlgorithm<J> algorithm;

  public MethodInvocationDerivator(CodeParser codeParser) {
    this.algorithm = new InvocationDerivatorAlgorithm<>(codeParser);
  }

  @Override
  public NewWritingPairs<J> derive(Writing<J> deriver, Writing<J> constant) {
    var methodInvocation = (MethodInvocation) deriver.writingExpression();
    var binding = methodInvocation.resolveMethodBinding();
    if (binding == null) {
      return NewWritingPairs.NULL();
    }

    var method = (IMethod) binding.getJavaElement();
    if (method == null) {
      return NewWritingPairs.NULL();
    }

    return algorithm.derive(deriver, constant, method);
  }
}



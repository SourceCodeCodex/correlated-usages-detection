package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.MethodInvocation;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.IWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.algorithm.InvocationDerivatorAlgorithm;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;

public final class MethodInvocationDerivator<J extends IJavaElement>
    implements IWritingsDerivator<J> {

  private final InvocationDerivatorAlgorithm<J> algorithm = new InvocationDerivatorAlgorithm<>();


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



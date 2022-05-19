package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.ISimpleWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.algorithm.InvocationDerivatorAlgorithm;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;

public final class SuperMethodInvocationDerivator<J extends IJavaElement>
    implements ISimpleWritingsDerivator<J> {

  private final InvocationDerivatorAlgorithm<J> algorithm = new InvocationDerivatorAlgorithm<>();

  @Override
  public NewWritingPairs<J> derive(DerivableWriting<J> deriver, Writing<J> constant) {
    var methodInvocation = (SuperMethodInvocation) deriver.writingExpression();
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



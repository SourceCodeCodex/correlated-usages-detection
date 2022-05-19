package upt.ac.cti.coverage.flow_insensitive.derivator.derivation;

import org.eclipse.jdt.core.IJavaElement;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;

public interface IComplexWritingsDerivator<J extends IJavaElement> {

  public abstract NewWritingPairs<J> derive(DerivableWriting<J> w1, DerivableWriting<J> w2);

}

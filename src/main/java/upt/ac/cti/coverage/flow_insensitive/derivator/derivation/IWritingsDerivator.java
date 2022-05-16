package upt.ac.cti.coverage.flow_insensitive.derivator.derivation;

import org.eclipse.jdt.core.IJavaElement;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;

public interface IWritingsDerivator<J extends IJavaElement> {

  public abstract NewWritingPairs<J> derive(Writing<J> w1, Writing<J> w2);

}

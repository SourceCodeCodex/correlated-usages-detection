package upt.ac.cti.coverage.derivator.derivation;

import org.eclipse.jdt.core.IJavaElement;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;

public interface IWritingsDerivator<J extends IJavaElement> {

  public abstract NewWritingPairs<J> derive(Writing<J> w1, Writing<J> w2);

}

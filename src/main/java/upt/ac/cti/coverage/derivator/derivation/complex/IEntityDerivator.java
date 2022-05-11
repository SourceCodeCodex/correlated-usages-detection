package upt.ac.cti.coverage.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;

interface IEntityDerivator<J extends IJavaElement> {
  public NewWritingPairs<J> derive(Writing<J> w);
}

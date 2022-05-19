package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;

class PPDifferentMethodDerivator<J extends IJavaElement> extends AComposedDerivator<J> {
  public PPDifferentMethodDerivator() {
    super(new PDerivator<>(), new PDerivator<>());
  }
}

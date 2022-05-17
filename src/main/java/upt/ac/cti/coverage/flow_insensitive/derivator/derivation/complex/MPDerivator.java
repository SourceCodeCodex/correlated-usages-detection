package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;

class MPDerivator<J extends IJavaElement> extends AComposedDerivator<J> {
  public MPDerivator() {
    super(new MDerivator<>(), new PDerivator<>());
  }
}

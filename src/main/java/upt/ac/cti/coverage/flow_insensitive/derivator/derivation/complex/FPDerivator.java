package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;

class FPDerivator<J extends IJavaElement> extends AComposedDerivator<J> {
  public FPDerivator() {
    super(new FDerivator<>(), new PDerivator<>());
  }
}

package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;

class FMDerivator<J extends IJavaElement> extends AComposedDerivator<J> {
  public FMDerivator() {
    super(new FDerivator<>(), new MDerivator<>());
  }
}

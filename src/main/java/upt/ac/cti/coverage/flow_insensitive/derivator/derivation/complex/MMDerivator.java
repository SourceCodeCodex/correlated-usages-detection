package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;

class MMDerivator<J extends IJavaElement> extends AComposedDerivator<J> {
  public MMDerivator() {
    super(new MDerivator<>(), new MDerivator<>());
  }
}

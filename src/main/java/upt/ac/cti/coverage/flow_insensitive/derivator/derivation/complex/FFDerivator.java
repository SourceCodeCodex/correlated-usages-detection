package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;

class FFDerivator<J extends IJavaElement> extends AComposedDerivator<J> {

  public FFDerivator() {
    super(new FDerivator<>(), new FDerivator<>());
  }

}

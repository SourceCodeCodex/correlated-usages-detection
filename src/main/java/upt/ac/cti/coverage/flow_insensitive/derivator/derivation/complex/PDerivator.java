package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.NameParameterDerivator;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;

class PDerivator<J extends IJavaElement> implements IEntityDerivator<J> {

  private final NameParameterDerivator<J> np = new NameParameterDerivator<>();

  @Override
  public NewWritingPairs<J> derive(DerivableWriting<J> w) {
    return np.derive(w, null);
  }

}

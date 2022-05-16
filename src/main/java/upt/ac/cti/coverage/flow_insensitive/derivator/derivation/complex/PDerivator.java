package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.NameParameterDerivator;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

class PDerivator<J extends IJavaElement> implements IEntityDerivator<J> {

  private final NameParameterDerivator<J> np;

  public PDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    np = new NameParameterDerivator<>(javaEntitySearcher, codeParser);
  }

  @Override
  public NewWritingPairs<J> derive(Writing<J> w) {
    return np.derive(w, null);
  }

}

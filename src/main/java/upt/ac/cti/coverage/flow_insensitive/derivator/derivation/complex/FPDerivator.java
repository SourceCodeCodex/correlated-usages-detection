package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

class FPDerivator<J extends IJavaElement> extends AComposedDerivator<J> {
  public FPDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    super(new FDerivator<>(javaEntitySearcher, codeParser),
        new PDerivator<>(javaEntitySearcher, codeParser));
  }
}

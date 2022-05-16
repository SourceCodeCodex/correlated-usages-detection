package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

class FMDerivator<J extends IJavaElement> extends AComposedDerivator<J> {
  public FMDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    super(new FDerivator<>(javaEntitySearcher, codeParser),
        new MDerivator<>(codeParser));
  }
}

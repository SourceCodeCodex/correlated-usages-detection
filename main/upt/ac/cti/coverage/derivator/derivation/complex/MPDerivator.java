package upt.ac.cti.coverage.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public class MPDerivator<J extends IJavaElement> extends AComposedDerivator<J> {

  public MPDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    super(new MDerivator<>(codeParser), new PDerivator<>(javaEntitySearcher, codeParser));
  }

}

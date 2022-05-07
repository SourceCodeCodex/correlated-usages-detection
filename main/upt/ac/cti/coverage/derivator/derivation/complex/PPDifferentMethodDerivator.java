package upt.ac.cti.coverage.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public class PPDifferentMethodDerivator<J extends IJavaElement> extends AComposedDerivator<J> {

  public PPDifferentMethodDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    super(new PDerivator<>(javaEntitySearcher, codeParser),
        new PDerivator<>(javaEntitySearcher, codeParser));
  }

}

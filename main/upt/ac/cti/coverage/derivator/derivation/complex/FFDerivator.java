package upt.ac.cti.coverage.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public class FFDerivator<J extends IJavaElement> extends AComposedDerivator<J> {

  public FFDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    super(new FDerivator<>(javaEntitySearcher, codeParser),
        new FDerivator<>(javaEntitySearcher, codeParser));
  }

}

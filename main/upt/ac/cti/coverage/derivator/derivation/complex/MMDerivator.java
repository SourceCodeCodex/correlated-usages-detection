package upt.ac.cti.coverage.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import upt.ac.cti.util.parsing.CodeParser;

public class MMDerivator<J extends IJavaElement> extends AComposedDerivator<J> {

  public MMDerivator(CodeParser codeParser) {
    super(new MDerivator<>(codeParser), new MDerivator<>(codeParser));
  }

}

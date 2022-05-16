package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import upt.ac.cti.util.parsing.CodeParser;

class MMDerivator<J extends IJavaElement> extends AComposedDerivator<J> {
  public MMDerivator(CodeParser codeParser) {
    super(new MDerivator<>(codeParser), new MDerivator<>(codeParser));
  }
}

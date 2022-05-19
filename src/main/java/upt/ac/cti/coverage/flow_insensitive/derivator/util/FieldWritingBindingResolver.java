package upt.ac.cti.coverage.flow_insensitive.derivator.util;

import org.eclipse.jdt.core.IField;
import upt.ac.cti.dependency.Dependencies;

public class FieldWritingBindingResolver extends ADerivableWritingBindingResolver<IField> {

  public FieldWritingBindingResolver() {
    super(Dependencies.fieldTypeBindingResolver);
  }

}

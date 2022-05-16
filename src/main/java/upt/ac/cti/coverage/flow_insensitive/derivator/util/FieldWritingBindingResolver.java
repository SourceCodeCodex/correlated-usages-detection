package upt.ac.cti.coverage.flow_insensitive.derivator.util;

import org.eclipse.jdt.core.IField;
import upt.ac.cti.util.binding.FieldTypeBindingResolver;
import upt.ac.cti.util.hierarchy.HierarchyResolver;

public class FieldWritingBindingResolver extends AWritingBindingResolver<IField> {

  public FieldWritingBindingResolver(HierarchyResolver hierarchyResolver,
      FieldTypeBindingResolver fieldTypeBindingResolver) {
    super(hierarchyResolver, fieldTypeBindingResolver);
  }

}

package upt.ac.cti.aperture;

import java.util.Set;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.util.binding.FieldTypeBindingResolver;
import upt.ac.cti.util.hierarchy.HierarchyResolver;

public class FieldAllTypePairsResolver extends AAllTypePairsResolver<IField> {

  public FieldAllTypePairsResolver(FieldTypeBindingResolver fieldTypeBindingResolver,
      HierarchyResolver hierarchyResolver) {
    super(fieldTypeBindingResolver, hierarchyResolver);
  }

  @Override
  public Set<Pair<IType, IType>> resolve(IField field1, IField field2) {
    return super.resolve(field1, field2);
  }

}

package upt.ac.cti.aperture;

import java.util.Set;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;
import upt.ac.cti.util.binding.ABindingResolver;

public class FieldAllTypePairsResolver extends AAllTypePairsResolver<IField> {

  public FieldAllTypePairsResolver(ABindingResolver<IField, ITypeBinding> aBindingResolver) {
    super(aBindingResolver);
  }

  @Override
  public Set<Pair<IType, IType>> resolve(IField field1, IField field2) {
    return super.resolve(field1, field2);
  }

}

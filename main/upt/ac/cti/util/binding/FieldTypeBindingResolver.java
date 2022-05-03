package upt.ac.cti.util.binding;

import java.util.Optional;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.ITypeBinding;
import upt.ac.cti.util.binding.visitor.FieldTypeBindingResolverVisitor;

public final class FieldTypeBindingResolver extends ABindingResolver<IField, ITypeBinding> {

  @Override
  public Optional<ITypeBinding> resolve(IField field) {
    var project = field.getJavaProject();
    var compilationUnit = field.getCompilationUnit();
    var visitor = new FieldTypeBindingResolverVisitor(field);

    return super.resolve(field, project, compilationUnit, visitor);
  }

}

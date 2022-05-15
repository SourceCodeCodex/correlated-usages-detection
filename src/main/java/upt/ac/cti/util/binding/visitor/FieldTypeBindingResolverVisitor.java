package upt.ac.cti.util.binding.visitor;

import java.util.Optional;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class FieldTypeBindingResolverVisitor extends ABindingResolverVisitor<IField, ITypeBinding> {

  public FieldTypeBindingResolverVisitor(IField field) {
    super(field);
  }

  @Override
  public boolean visit(VariableDeclarationFragment node) {
    var variableBinding = node.resolveBinding();
    if (variableBinding != null && javaElement.equals(variableBinding.getJavaElement())) {
      this.binding = Optional.ofNullable(variableBinding.getType());
    }
    return false;
  }

}

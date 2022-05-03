package upt.ac.cti.util.binding.visitor;

import java.util.Optional;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class FieldTypeBindingResolverVisitor extends ABindingResolverVisitor<IField, ITypeBinding> {

  public FieldTypeBindingResolverVisitor(IField member) {
    super(member);
  }

  @Override
  public boolean visit(VariableDeclarationFragment node) {
    var variableBinding = node.resolveBinding();
    if (member.equals(variableBinding.getJavaElement())) {
      this.binding = Optional.ofNullable(variableBinding.getType());
      return false;
    }
    return true;
  }

}

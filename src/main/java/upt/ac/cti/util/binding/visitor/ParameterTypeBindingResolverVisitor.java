package upt.ac.cti.util.binding.visitor;

import java.util.Optional;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class ParameterTypeBindingResolverVisitor
    extends ABindingResolverVisitor<ILocalVariable, ITypeBinding> {

  public ParameterTypeBindingResolverVisitor(ILocalVariable parameter) {
    super(parameter);
  }

  @Override
  public boolean visit(SingleVariableDeclaration node) {
    var variableBinding = node.resolveBinding();
    if (javaElement.equals(variableBinding.getJavaElement())) {
      this.binding = Optional.ofNullable(variableBinding.getType());
    }
    return false;
  }

}

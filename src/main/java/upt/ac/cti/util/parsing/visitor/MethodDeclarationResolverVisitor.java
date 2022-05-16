package upt.ac.cti.util.parsing.visitor;

import java.util.Optional;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodDeclarationResolverVisitor
    extends AASTNodeResolverVisitor<IMethod, MethodDeclaration> {


  public MethodDeclarationResolverVisitor(IMethod method) {
    super(method);
  }

  @Override
  public boolean visit(MethodDeclaration node) {
    if (node.resolveBinding() != null && member.equals(node.resolveBinding().getJavaElement())) {
      result = Optional.of(node);
    }
    return true;
  }

}

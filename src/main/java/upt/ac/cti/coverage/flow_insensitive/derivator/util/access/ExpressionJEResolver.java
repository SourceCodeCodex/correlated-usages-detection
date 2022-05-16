package upt.ac.cti.coverage.flow_insensitive.derivator.util.access;

import java.util.Optional;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SuperFieldAccess;

class ExpressionJEResolver {
  public static Optional<IJavaElement> resolve(Expression expression) {
    var binding = Optional.ofNullable(switch (expression.getNodeType()) {

      case ASTNode.SIMPLE_NAME:
      case ASTNode.QUALIFIED_NAME:
        yield ((Name) expression).resolveBinding();
      case ASTNode.SUPER_FIELD_ACCESS:
        yield ((SuperFieldAccess) expression).resolveFieldBinding();
      case ASTNode.FIELD_ACCESS:
        yield ((FieldAccess) expression).resolveFieldBinding();
      default:
        yield null;

    });

    return binding.map(IBinding::getJavaElement);

  }

}

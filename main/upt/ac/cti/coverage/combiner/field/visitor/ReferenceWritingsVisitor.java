package upt.ac.cti.coverage.combiner.field.visitor;

import java.util.Optional;
import java.util.logging.Logger;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.util.logging.RLogger;

public class ReferenceWritingsVisitor extends AFieldWritingsVisitor {

  private static final Logger logger = RLogger.get();

  public ReferenceWritingsVisitor(IField field, IMethod scope) {
    super(field, scope);
  }

  @Override
  public boolean visit(Assignment node) {
    var left = node.getLeftHandSide();

    switch (left.getNodeType()) {

      case ASTNode.SIMPLE_NAME: {
        var binding = ((SimpleName) left).resolveBinding();
        if (binding instanceof IVariableBinding varBinding) {
          if (field.equals(varBinding.getJavaElement())) {
            var fieldWrite =
                new Writing<>(field, node.getRightHandSide(), Optional.empty(), scope);
            result.add(fieldWrite);
          }
        }
        return true;
      }

      case ASTNode.QUALIFIED_NAME: {
        var binding = ((QualifiedName) left).resolveBinding();
        var qualifier = ((QualifiedName) left).getQualifier();
        if (binding instanceof IVariableBinding varBinding) {
          if (field.equals(varBinding.getJavaElement())) {
            var fieldWrite =
                new Writing<>(field, node.getRightHandSide(), Optional.of(qualifier), scope);
            result.add(fieldWrite);
          }
        }
        return true;
      }

      case ASTNode.SUPER_FIELD_ACCESS: {
        var binding = ((SuperFieldAccess) left).resolveFieldBinding();
        if (field.equals(binding.getJavaElement())) {
          var fieldWrite =
              new Writing<>(field, node.getRightHandSide(), Optional.empty(), scope);
          result.add(fieldWrite);
        }
        return true;
      }

      case ASTNode.FIELD_ACCESS: {
        var binding = ((FieldAccess) left).resolveFieldBinding();

        if (field.equals(binding.getJavaElement())) {
          var accessExpression = ((FieldAccess) left).getExpression();
          if (accessExpression.getNodeType() == ASTNode.THIS_EXPRESSION) {
            var fieldWrite =
                new Writing<>(field, node.getRightHandSide(), Optional.empty(), scope);
            result.add(fieldWrite);
          } else {
            // Although the field access expression can have a manifold of posibilities, we take
            // into consideration only the most plausible scenarios.
            var fieldWrite =
                new Writing<>(field, node.getRightHandSide(), Optional.of(accessExpression),
                    scope);
            result.add(fieldWrite);
          }
        }
        return true;
      }

      case ASTNode.ARRAY_ACCESS: {
        return true;
      }

      default: {
        logger.warning(
            "Assignment discarded as left hand side type is not resolved yet: "
                + left.getNodeType());
        return true;
      }
    }
  }

}

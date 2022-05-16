package upt.ac.cti.coverage.flow_insensitive.combiner.field.visitor;

import java.util.logging.Logger;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.util.computation.Either;
import upt.ac.cti.util.logging.RLogger;

public class ReferenceWritingsVisitor extends AFieldWritingsVisitor {

  private static final Logger logger = RLogger.get();

  public ReferenceWritingsVisitor(IField field) {
    super(field);
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
                new Writing<>(field, node.getRightHandSide(),
                    Either.left(field.getDeclaringType()));
            result.add(fieldWrite);
          }
        }
        break;
      }

      case ASTNode.QUALIFIED_NAME: {
        var binding = ((QualifiedName) left).resolveBinding();
        var qualifier = ((QualifiedName) left).getQualifier();
        if (binding instanceof IVariableBinding varBinding) {
          if (field.equals(varBinding.getJavaElement())) {
            var fieldWrite =
                new Writing<>(field, node.getRightHandSide(), Either.right(qualifier));
            result.add(fieldWrite);
          }
        }
        break;
      }

      case ASTNode.SUPER_FIELD_ACCESS: {
        var binding = ((SuperFieldAccess) left).resolveFieldBinding();
        if (field.equals(binding.getJavaElement())) {
          var fieldWrite =
              new Writing<>(field, node.getRightHandSide(), Either.left(field.getDeclaringType()));
          result.add(fieldWrite);
        }
        break;
      }

      case ASTNode.FIELD_ACCESS: {
        var binding = ((FieldAccess) left).resolveFieldBinding();

        if (field.equals(binding.getJavaElement())) {
          var accessExpression = ((FieldAccess) left).getExpression();
          if (accessExpression.getNodeType() == ASTNode.THIS_EXPRESSION) {
            var fieldWrite =
                new Writing<>(field, node.getRightHandSide(),
                    Either.left(field.getDeclaringType()));
            result.add(fieldWrite);
          } else {
            var fieldWrite =
                new Writing<>(field, node.getRightHandSide(), Either.right(accessExpression));
            result.add(fieldWrite);
          }
        }
        break;
      }

      case ASTNode.ARRAY_ACCESS: {
        break;
      }

      default: {
        logger.warning(
            "Assignment discarded as left hand side type is not resolved yet: "
                + left.getNodeType());
      }
    }
    return true;
  }

}

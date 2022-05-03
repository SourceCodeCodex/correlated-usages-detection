package upt.ac.cti.coverage.derivator.internal.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import upt.ac.cti.coverage.model.Writing;

public class FieldAssignmentVisitor extends ASTVisitor {

  private final Writing deriver;
  private final IField field;

  private final List<Writing> derivations = new ArrayList<>();

  private static final Logger logger = Logger.getLogger(FieldAssignmentVisitor.class.getName());

  public FieldAssignmentVisitor(Writing deriver, IField field) {
    this.deriver = deriver;
    this.field = field;
  }

  public List<Writing> derivations() {
    return derivations;
  }

  @Override
  public boolean visit(Assignment node) {
    var left = node.getLeftHandSide();
    switch (left.getNodeType()) {
      case ASTNode.SIMPLE_NAME: {
        var binding = ((SimpleName) left).resolveBinding();
        if (binding != null && binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (field.equals(varBinding.getJavaElement())) {
            derivations.add(deriver.withWritingExpression(node.getRightHandSide()));

          }
        }
        return true;
      }

      case ASTNode.QUALIFIED_NAME: {
        var binding = ((QualifiedName) left).resolveBinding();
        if (binding != null && binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (field.equals(varBinding.getJavaElement())) {
            derivations.add(deriver.withWritingExpression(node.getRightHandSide()));
          }
        }
        return true;
      }

      case ASTNode.SUPER_FIELD_ACCESS: {
        var binding = ((SuperFieldAccess) left).resolveFieldBinding();

        if (binding != null && field.equals(binding.getJavaElement())) {
          derivations.add(deriver.withWritingExpression(node.getRightHandSide()));
        }
        return true;
      }

      case ASTNode.FIELD_ACCESS: {
        var binding = ((FieldAccess) left).resolveFieldBinding();

        if (binding != null && field.equals(binding.getJavaElement())) {
          derivations.add(deriver.withWritingExpression(node.getRightHandSide()));
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

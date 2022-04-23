package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

public class FieldAssignmentVisitor extends ASTVisitor {

  private final IField field;

  private final List<Expression> result = new ArrayList<>();

  private static final Logger logger = Logger.getLogger(FieldAssignmentVisitor.class.getName());

  public FieldAssignmentVisitor(IField field) {
    this.field = field;
  }

  public List<Expression> result() {
    return result;
  }

  @Override
  public boolean visit(Assignment node) {
    var left = node.getLeftHandSide();
    switch (left.getNodeType()) {
      case ASTNode.SIMPLE_NAME: {
        var binding = ((SimpleName) left).resolveBinding();
        if (binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (field.equals(varBinding.getJavaElement())) {
            result.add(node.getRightHandSide());

          }
        }
        break;
      }
      case ASTNode.QUALIFIED_NAME: {
        var binding = ((QualifiedName) left).resolveBinding();
        if (binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (field.equals(varBinding.getJavaElement())) {
            result.add(node.getRightHandSide());

          }
        }
        break;
      }
      case ASTNode.FIELD_ACCESS: {
        var fieldBinding = ((FieldAccess) left).resolveFieldBinding();
        if (field.equals(fieldBinding.getJavaElement())) {
          result.add(node.getRightHandSide());
        }
        break;
      }
      default: {
        if (field.equals(left.resolveTypeBinding().getJavaElement())) {
          logger.warning(
              "Assignment omitted as there is no handling method for assignments with this left side expression type: "
                  + left.getNodeType());
        }
      }
    }
    return true;
  }
}

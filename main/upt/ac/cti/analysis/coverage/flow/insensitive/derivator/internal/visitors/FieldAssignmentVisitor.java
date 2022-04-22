package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

public class FieldAssignmentVisitor extends ASTVisitor {

  private final IField iField;

  private final List<Expression> result = new ArrayList<>();

  public FieldAssignmentVisitor(IField iField) {
    this.iField = iField;
  }

  @Override
  public boolean visit(Assignment node) {
    return handleLefSide(node, node.getLeftHandSide());
  }

  public List<Expression> result() {
    return result;
  }

  private boolean handleLefSide(Assignment node, Expression leftSide) {
    var leftExpressionType = leftSide.getNodeType();
    switch (leftExpressionType) {
      case ASTNode.SIMPLE_NAME: {
        var binding = ((SimpleName) leftSide).resolveBinding();
        if (binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (iField.equals(varBinding.getJavaElement())) {
            result.add(node.getRightHandSide());

          }
        }
        break;
      }
      case ASTNode.QUALIFIED_NAME: {
        var binding = ((QualifiedName) leftSide).resolveBinding();
        if (binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (iField.equals(varBinding.getJavaElement())) {
            result.add(node.getRightHandSide());

          }
        }
        break;
      }
      case ASTNode.FIELD_ACCESS: {
        var varBinding = ((FieldAccess) leftSide).resolveFieldBinding();
        if (iField.equals(varBinding.getJavaElement())) {
          result.add(node.getRightHandSide());
        }
        break;
      }
      case ASTNode.PARENTHESIZED_EXPRESSION: {
        return handleLefSide(node, ((ParenthesizedExpression) leftSide).getExpression());
      }
    }
    return true;
  }

}

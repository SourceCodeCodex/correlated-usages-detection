package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class LocalVariableAssignmentVisitor extends ASTVisitor {

  private final ILocalVariable localVar;

  private final List<Expression> result = new ArrayList<>();

  public LocalVariableAssignmentVisitor(ILocalVariable localVar) {
    this.localVar = localVar;
  }

  public List<Expression> result() {
    return result;
  }

  @Override
  public boolean visit(SingleVariableDeclaration node) {
    if (localVar.equals(node.resolveBinding().getJavaElement())) {
      if (node.getInitializer() != null) {
        result.add(node.getInitializer());
      }
    }
    return true;
  }

  @Override
  public boolean visit(VariableDeclarationFragment node) {
    if (localVar.equals(node.resolveBinding().getJavaElement())) {
      if (node.getInitializer() != null) {
        result.add(node.getInitializer());
      }
    }
    return true;
  }

  @Override
  public boolean visit(Assignment node) {
    return handleAssignmentLefSide(node, node.getLeftHandSide());
  }

  // Needed in case of parenthesized epxression - recursive
  private boolean handleAssignmentLefSide(Assignment node, Expression leftSide) {
    var leftExpressionType = leftSide.getNodeType();
    switch (leftExpressionType) {
      case ASTNode.SIMPLE_NAME: {
        var binding = ((SimpleName) leftSide).resolveBinding();
        if (binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (localVar.equals(varBinding.getJavaElement())) {
            result.add(node.getRightHandSide());
          }
        }
        break;
      }
      case ASTNode.PARENTHESIZED_EXPRESSION: {
        return handleAssignmentLefSide(node, ((ParenthesizedExpression) leftSide).getExpression());
      }
    }
    return true;
  }


}

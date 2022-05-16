package upt.ac.cti.coverage.flow_insensitive.derivator.util;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import org.javatuples.Pair;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.priority.DerivationPriority;

public class DerivationPrioritizer<J extends IJavaElement> {
  public DerivationPriority prioritize(Pair<Writing<J>, Writing<J>> pair) {

    var check1 = isOnScopeBoundary(pair.getValue0());
    var check2 = isOnScopeBoundary(pair.getValue1());

    if (!check1) {
      return DerivationPriority.DERIVATE_FIRST;
    }

    if (!check2) {
      return DerivationPriority.DERIVATE_SECOND;
    }

    return DerivationPriority.COMPLEX_PRIORITY;
  }

  private boolean isOnScopeBoundary(Writing<J> w) {
    var expressionType = w.writingExpression().getNodeType();
    switch (expressionType) {
      case ASTNode.FIELD_ACCESS: {
        return true;
      }
      case ASTNode.METHOD_INVOCATION: {
        return true;
      }
      case ASTNode.SUPER_FIELD_ACCESS: {
        return true;
      }
      case ASTNode.SUPER_METHOD_INVOCATION: {
        return true;
      }
      case ASTNode.ASSIGNMENT: {
        return false;
      }
      case ASTNode.PARENTHESIZED_EXPRESSION: {
        return false;
      }
      case ASTNode.CONDITIONAL_EXPRESSION: {
        return false;
      }
      case ASTNode.CAST_EXPRESSION: {
        return false;
      }
      case ASTNode.SIMPLE_NAME:
      case ASTNode.QUALIFIED_NAME: {
        var name = (Name) w.writingExpression();
        var varBinding = (IVariableBinding) name.resolveBinding();

        return varBinding.isParameter() || varBinding.isField();
      }
    }
    return false;
  }

}

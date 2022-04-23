package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class LocalVariableAssignmentVisitor extends ASTVisitor {

  private final ILocalVariable localVar;

  private final List<Expression> result = new ArrayList<>();

  private static final Logger logger =
      Logger.getLogger(LocalVariableAssignmentVisitor.class.getName());

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
    var left = node.getLeftHandSide();
    switch (left.getNodeType()) {
      case ASTNode.SIMPLE_NAME: {
        var binding = ((SimpleName) left).resolveBinding();
        if (binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (localVar.equals(varBinding.getJavaElement())) {
            result.add(node.getRightHandSide());
          }
        }
        break;
      }
      default: {
        if (localVar.equals(left.resolveTypeBinding().getJavaElement())) {
          logger.warning(
              "Assignment omitted as there is no handling method for assignments with this left side expression type: "
                  + left.getNodeType());
        }
      }

    }
    return true;
  }


}

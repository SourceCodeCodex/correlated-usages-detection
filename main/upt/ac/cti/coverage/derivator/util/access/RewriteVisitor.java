package upt.ac.cti.coverage.derivator.util.access;

import java.util.Optional;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

class RewriteVisitor extends ASTVisitor {

  private final IJavaElement javaElement;
  private final Expression accessExpression;
  private boolean stop = false;

  private Optional<Expression> rewriting = Optional.empty();

  public RewriteVisitor(IJavaElement javaElement, Expression accessExpression) {
    this.javaElement = javaElement;
    this.accessExpression = accessExpression;
  }

  public Optional<Expression> rewriting() {
    return rewriting;
  }


  @Override
  public boolean preVisit2(ASTNode node) {
    if (!stop && node.equals(accessExpression)) {
      stop = true;
    }
    return !stop;
  }

  @Override
  public boolean visit(VariableDeclarationFragment node) {
    var binding = node.resolveBinding();
    if (binding != null && javaElement.equals(binding.getJavaElement())) {
      if (node.getInitializer() != null) {
        rewriting = Optional.of(node.getInitializer());
      }
    }
    return false;
  }

  @Override
  public boolean visit(Assignment node) {
    var je = ExpressionJEResolver.resolve(node.getLeftHandSide());
    if (je.isPresent() && je.get().equals(javaElement)) {
      rewriting = Optional.of(node.getRightHandSide());
    }
    return false;
  }
}

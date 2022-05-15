package upt.ac.cti.coverage.derivator.util.access;

import java.util.function.Predicate;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.javatuples.Pair;
import upt.ac.cti.coverage.model.Writing;

public class SameAccessExpressionValidator<J extends IJavaElement>
    implements Predicate<Pair<Writing<J>, Writing<J>>> {

  @Override
  public boolean test(Pair<Writing<J>, Writing<J>> pair) {

    var accessExpr1 = pair.getValue0().accessExpression();
    var accessExpr2 = pair.getValue1().accessExpression();

    if (accessExpr1.isLeft() && accessExpr2.isLeft()) {
      return accessExpr1.getLeft().equals(accessExpr2.getLeft());
    }

    if (accessExpr1.isRight() && accessExpr2.isRight()) {

      var expr1 = accessExpr1.getRight();
      var expr2 = accessExpr2.getRight();

      return testExpressions(expr1, expr2);
    }

    if (accessExpr1.isLeft() && accessExpr2.isRight()) {
      return testCrossBorder(accessExpr1.getLeft(), accessExpr2.getRight());
    }

    return testCrossBorder(accessExpr2.getLeft(), accessExpr1.getRight());
  }

  private boolean testCrossBorder(IMember thisMember, Expression expr) {
    if (expr.getNodeType() != ASTNode.CLASS_INSTANCE_CREATION) {
      return false;
    }

    var type = ((ClassInstanceCreation) expr).resolveTypeBinding().getJavaElement();
    return type != null && thisMember.equals(type);
  }

  private boolean testExpressions(Expression expr1, Expression expr2) {

    if (expr1.equals(expr2)) {
      return true;
    }

    if (expr1.getNodeType() == ASTNode.CLASS_INSTANCE_CREATION
        || expr2.getNodeType() == ASTNode.CLASS_INSTANCE_CREATION
        || expr1.getNodeType() == ASTNode.NULL_LITERAL
        || expr2.getNodeType() == ASTNode.NULL_LITERAL) {
      return false;
    }

    if (expr1.getNodeType() != expr2.getNodeType() || !expr1.toString().equals(expr2.toString())) {
      return false;
    }

    if (!expr1.getRoot().equals(expr2.getRoot())) {
      return false;
    }

    var javaEl1 = ExpressionJEResolver.resolve(expr1);
    var javaEl2 = ExpressionJEResolver.resolve(expr2);

    if (!javaEl1.equals(javaEl2)) {
      return false;
    }

    if (javaEl1.isPresent() && javaEl2.isPresent()) {
      var root = expr1.getRoot();
      var v1 = new RewriteVisitor(javaEl1.get(), expr1);
      var v2 = new RewriteVisitor(javaEl2.get(), expr2);

      root.accept(v1);
      root.accept(v2);

      if (v1.rewriting().isEmpty() && v2.rewriting().isEmpty()) {
        ASTNode b1 = expr1, b2 = expr2;
        while (b1.getNodeType() != ASTNode.BLOCK) {
          b1 = b1.getParent();
        }
        while (b2.getNodeType() != ASTNode.BLOCK) {
          b2 = b2.getParent();
        }
        return b1.equals(b2);
      }

      if (v1.rewriting().isPresent() && v2.rewriting().isPresent()) {
        var rw1 = v1.rewriting().get();
        var rw2 = v2.rewriting().get();
        return testExpressions(rw1, rw2);
      }

    }

    return false;
  }

}

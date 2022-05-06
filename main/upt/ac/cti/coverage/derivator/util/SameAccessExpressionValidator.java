package upt.ac.cti.coverage.derivator.util;

import java.util.function.Predicate;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.Expression;
import org.javatuples.Pair;
import upt.ac.cti.coverage.model.Writing;

// TODO: Improve accesing object validation
public class SameAccessExpressionValidator<J extends IJavaElement>
    implements Predicate<Pair<Writing<J>, Writing<J>>> {

  @Override
  public boolean test(Pair<Writing<J>, Writing<J>> pair) {
    var accessExpr1 = pair.getValue0().accessExpression();
    var accessExpr2 = pair.getValue1().accessExpression();

    var areAccessingExpressionsEqual = accessExpr1.equals(accessExpr2);

    var areAccessingExpressionsBindingsEqual = accessExpr1.map(Expression::resolveTypeBinding)
        .equals(accessExpr2.map(Expression::resolveTypeBinding));

    return areAccessingExpressionsEqual || areAccessingExpressionsBindingsEqual;
  }

}

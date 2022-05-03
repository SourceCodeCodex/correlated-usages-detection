package upt.ac.cti.coverage.model;

import java.util.Optional;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.Expression;

public final record Writing(IJavaElement javaElement, Expression writingExpression,
    Optional<Expression> accessExpression) {

  public Writing withWritingExpression(Expression writingExpression) {
    return new Writing(javaElement, writingExpression, accessExpression);
  }

  public Writing withAccessExpression(Expression accessExpression) {
    return new Writing(javaElement, writingExpression, Optional.of(accessExpression));
  }

}

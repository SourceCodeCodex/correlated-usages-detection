package upt.ac.cti.coverage.model;

import java.util.Optional;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.dom.Expression;

public final record Writing<T extends IJavaElement> (
    T element,
    Expression writingExpression,
    Optional<Expression> accessExpression,
    IMember scope,
    int depth) {

  public Writing(
      T element,
      Expression writingExpression,
      Optional<Expression> accessExpression,
      IMember scope) {
    this(element, writingExpression, accessExpression, scope, 0);
  }

  public Writing<T> withWritingExpression(Expression writingExpression) {
    return new Writing<>(element, writingExpression, accessExpression, scope, depth);
  }

  public Writing<T> withAccessExpression(Expression accessExpression) {
    return new Writing<>(element, writingExpression, Optional.of(accessExpression), scope,
        depth);
  }

  public Writing<T> withScope(IMember scope) {
    return new Writing<>(element, writingExpression, accessExpression, scope, depth);
  }

  public Writing<T> increaseDepth() {
    return new Writing<>(element, writingExpression, accessExpression, scope, depth + 1);
  }

  @Override
  public String toString() {
    return "Writing [element=" + element.getElementName() + ", writingExpression="
        + writingExpression
        + ", accessExpression=" + accessExpression + ", scope=" + scope.getElementName()
        + ", depth=" + depth + "]";
  }



}

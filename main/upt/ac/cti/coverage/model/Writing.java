package upt.ac.cti.coverage.model;

import java.util.Optional;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.Expression;

public final record Writing<T extends IJavaElement> (
    T element,
    Expression writingExpression,
    Optional<Expression> accessExpression,
    int depth) {

  public Writing(
      T element,
      Expression writingExpression,
      Optional<Expression> accessExpression) {
    this(element, writingExpression, accessExpression, 0);
  }

  public Writing<T> withWritingExpression(Expression writingExpression) {
    return new Writing<>(element, writingExpression, accessExpression, depth);
  }

  public Writing<T> withAccessExpression(Expression accessExpression) {
    return new Writing<>(element, writingExpression, Optional.ofNullable(accessExpression),
        depth);
  }

  public Writing<T> increaseDepth() {
    return new Writing<>(element, writingExpression, accessExpression, depth + 1);
  }

  @Override
  public String toString() {
    return "Writing [element=" + element.getElementName() + ", writingExpression="
        + writingExpression
        + ", accessExpression=" + accessExpression
        + ", depth=" + depth + "]";
  }



}

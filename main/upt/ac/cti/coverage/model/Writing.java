package upt.ac.cti.coverage.model;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.dom.Expression;
import upt.ac.cti.util.Either;

public final record Writing<T extends IJavaElement> (
    T element,
    Expression writingExpression,
    Either<IMember, Expression> accessExpression,
    int depth) {

  public Writing(
      T element,
      Expression writingExpression,
      Either<IMember, Expression> accessExpression) {
    this(element, writingExpression, accessExpression, 0);
  }

  public Writing<T> withWritingExpression(Expression writingExpression) {
    return new Writing<>(element, writingExpression, accessExpression, depth);
  }

  public Writing<T> withAccessExpression(Either<IMember, Expression> accessExpression) {
    return new Writing<>(element, writingExpression, accessExpression,
        depth);
  }

  public Writing<T> increaseDepth() {
    return new Writing<>(element, writingExpression, accessExpression, depth + 1);
  }

  @Override
  public String toString() {
    return "Writing [element=" + element.getElementName() + ", writingExpression="
        + writingExpression
        + ", accessExpression=" + accessExpression.mapLeft(IMember::getElementName).mapRight(e -> e)
        + ", depth=" + depth
        + ", binding=" + writingExpression.resolveTypeBinding().getQualifiedName() +
        "]";
  }



}

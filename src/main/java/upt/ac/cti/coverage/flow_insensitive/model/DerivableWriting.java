package upt.ac.cti.coverage.flow_insensitive.model;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.dom.Expression;
import upt.ac.cti.util.computation.Either;

public final record DerivableWriting<T extends IJavaElement> (
    T element,
    Expression writingExpression,
    Either<IMember, Expression> accessExpression,
    int depth) implements Writing<T> {

  public DerivableWriting(
      T element,
      Expression writingExpression,
      Either<IMember, Expression> accessExpression) {
    this(element, writingExpression, accessExpression, 0);
  }

  public UnderivableWriting<T> toUnderivableWriting() {
    return new UnderivableWriting<>(element, writingExpression, depth);
  }

  public DerivableWriting<T> withWritingExpression(Expression writingExpression) {
    return new DerivableWriting<>(element, writingExpression, accessExpression, depth);
  }

  public DerivableWriting<T> withAccessExpression(Either<IMember, Expression> accessExpression) {
    return new DerivableWriting<>(element, writingExpression, accessExpression,
        depth);
  }

  public DerivableWriting<T> increaseDepth() {
    return new DerivableWriting<>(element, writingExpression, accessExpression, depth + 1);
  }

  @Override
  public String toString() {
    return "Writing [element=" + element.getElementName() + ", writingExpression="
        + writingExpression
        + ", accessExpression=" + accessExpression.mapLeft(IMember::getElementName).mapRight(e -> e)
        + ", depth=" + depth +
        "]";
  }



}

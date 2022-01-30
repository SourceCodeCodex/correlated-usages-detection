package upt.ac.cti.coverage.analysis.iterative.model;

import java.util.Objects;
import org.eclipse.jdt.core.dom.Expression;

public final class ConcreteObject extends BaseObject {

  private final Expression expression;

  public ConcreteObject(Expression expression) {
    super();
    this.expression = expression;
  }

  @Override
  public boolean isThis() {
    return false;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public int hashCode() {
    return Objects.hash(expression);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    var other = (ConcreteObject) obj;
    return expression.equals(other.expression)
        || expression.resolveTypeBinding().equals(other.expression.resolveTypeBinding());
  }

  @Override
  public String toString() {
    return "ConcreteObject(" + expression + ")";
  }



}

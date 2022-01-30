package upt.ac.cti.coverage.analysis.iterative.model;

import java.util.Objects;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.Expression;

public record FieldAsgmt(IField iField, IMethod currentMethod, Expression rightSide,
    BaseObject baseObject) {

  public FieldAsgmt withCurrentMethod(IMethod newMethod) {
    return new FieldAsgmt(iField, newMethod, rightSide, baseObject);
  }

  public FieldAsgmt withRightSide(Expression newRightSide) {
    return new FieldAsgmt(iField, currentMethod, newRightSide, baseObject);
  }

  public FieldAsgmt withBaseObject(BaseObject newBaseObject) {
    return new FieldAsgmt(iField, currentMethod, rightSide, newBaseObject);
  }

  public static class Builder {
    private IField iField;
    private IMethod currentMethod;
    private Expression rightSide;
    private BaseObject baseObject;

    public Builder currentMethod(IMethod currentMethod) {
      this.currentMethod = currentMethod;
      return this;
    }

    public Builder rightSide(Expression rightSide) {
      this.rightSide = rightSide;
      return this;
    }

    public Builder field(IField iField) {
      this.iField = iField;
      return this;
    }

    public Builder baseObject(BaseObject baseObject) {
      this.baseObject = baseObject;
      return this;
    }

    public FieldAsgmt build() {
      if (iField == null || currentMethod == null || rightSide == null
          || baseObject == null) {
        throw new Error("Required field not set!");
      }
      return new FieldAsgmt(iField, currentMethod, rightSide, baseObject);

    }

  }

  public static Builder builder() {
    return new Builder();
  }


  @Override
  public String toString() {
    return "FieldAsgmtData [iField=" + iField.getElementName() + ", currentMethod="
        + currentMethod.getElementName()
        + ", rightSide=" + rightSide + ", baseObject=" + baseObject + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseObject, currentMethod, iField, rightSide);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof FieldAsgmt other)) {
      return false;
    }
    return Objects.equals(baseObject, other.baseObject)
        && Objects.equals(currentMethod, other.currentMethod)
        && Objects.equals(iField, other.iField)
        && Objects.equals(rightSide, other.rightSide);
  }

}

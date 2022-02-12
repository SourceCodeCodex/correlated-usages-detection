package upt.ac.cti.coverage.analysis.flow.insensitive.model;

import java.util.Objects;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.Expression;

public record FieldAsgmt(IField iField, Expression rightSide,
    BaseObject baseObject) {

  public FieldAsgmt withRightSide(Expression newRightSide) {
    return new FieldAsgmt(iField, newRightSide, baseObject);
  }

  public FieldAsgmt withBaseObject(BaseObject newBaseObject) {
    return new FieldAsgmt(iField, rightSide, newBaseObject);
  }

  public static class Builder {
    private IField iField;
    private Expression rightSide;
    private BaseObject baseObject;

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
      if (iField == null || rightSide == null
          || baseObject == null) {
        throw new Error("Required field not set!");
      }
      return new FieldAsgmt(iField, rightSide, baseObject);

    }

  }

  public static Builder builder() {
    return new Builder();
  }


  @Override
  public String toString() {
    return "FieldAsgmtData [iField=" + iField.getElementName()
        + ", rightSide=" + rightSide + ", baseObject=" + baseObject + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseObject, iField, rightSide);
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
        && Objects.equals(iField, other.iField)
        && Objects.equals(rightSide, other.rightSide);
  }

}

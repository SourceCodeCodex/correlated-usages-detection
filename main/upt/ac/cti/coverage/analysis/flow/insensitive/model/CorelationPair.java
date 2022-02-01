package upt.ac.cti.coverage.analysis.flow.insensitive.model;

import java.util.Objects;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ITypeBinding;

public record CorelationPair(FieldAsgmt field1Asgmt,
    FieldAsgmt field2Asgmt, Optional<ITypeBinding> field1Binding,
    Optional<ITypeBinding> field2Binding) {


  public FieldAsgmt fieldAsgmt(CPIndex index) {
    switch (index) {
      case FIRST: {
        return field1Asgmt;
      }
      default:
        return field2Asgmt;
    }
  }

  public Optional<ITypeBinding> fieldBinding(CPIndex index) {
    switch (index) {
      case FIRST: {
        return field1Binding;
      }
      default:
        return field2Binding;
    }
  }

  public CorelationPair withFieldAsgmt(FieldAsgmt newFieldAsgmt, CPIndex index) {
    switch (index) {
      case FIRST: {
        return new CorelationPair(newFieldAsgmt, field2Asgmt, field1Binding, field2Binding);
      }
      default:
        return new CorelationPair(field1Asgmt, newFieldAsgmt, field1Binding, field2Binding);
    }
  }

  public CorelationPair withFieldBinding(Optional<ITypeBinding> newFieldBinding, CPIndex index) {
    switch (index) {
      case FIRST: {
        return new CorelationPair(field1Asgmt, field2Asgmt, newFieldBinding, field2Binding);
      }
      default:
        return new CorelationPair(field1Asgmt, field2Asgmt, field1Binding, newFieldBinding);
    }
  }


  @Override
  public String toString() {
    return "CorelationPair [field1Data=" + field1Asgmt + ", field2Data=" + field2Asgmt
        + ", binding1=" + field1Binding.map(ITypeBinding::getName) + ", binding2="
        + field2Binding.map(ITypeBinding::getName) + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(field1Asgmt, field2Asgmt);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CorelationPair other)) {
      return false;
    }
    return Objects.equals(field1Asgmt, other.field1Asgmt)
        && Objects.equals(field2Asgmt, other.field2Asgmt);
  }


}

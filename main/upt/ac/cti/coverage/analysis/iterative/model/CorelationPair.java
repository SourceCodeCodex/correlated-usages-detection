package upt.ac.cti.coverage.analysis.iterative.model;

import java.util.Objects;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ITypeBinding;

public record CorelationPair(FieldAsgmt field1Asgmt,
    FieldAsgmt field2Asgmt, Optional<ITypeBinding> field1Binding,
    Optional<ITypeBinding> field2Binding) {


  public CorelationPair withField1Data(FieldAsgmt newField1Data) {
    return new CorelationPair(newField1Data, field2Asgmt, field1Binding, field2Binding);
  }

  public CorelationPair withField2Data(FieldAsgmt newField2Data) {
    return new CorelationPair(field1Asgmt, newField2Data, field1Binding, field2Binding);
  }

  public CorelationPair withField1Binding(Optional<ITypeBinding> newField1Binding) {
    return new CorelationPair(field1Asgmt, field2Asgmt, newField1Binding, field2Binding);
  }

  public CorelationPair withField2Binding(Optional<ITypeBinding> newField2Binding) {
    return new CorelationPair(field1Asgmt, field2Asgmt, field1Binding, newField2Binding);
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

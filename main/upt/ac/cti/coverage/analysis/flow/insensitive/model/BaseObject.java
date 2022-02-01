package upt.ac.cti.coverage.analysis.flow.insensitive.model;


public abstract class BaseObject {
  public abstract boolean isThis();

  @Override
  public abstract int hashCode();

  @Override
  public abstract boolean equals(Object obj);
}

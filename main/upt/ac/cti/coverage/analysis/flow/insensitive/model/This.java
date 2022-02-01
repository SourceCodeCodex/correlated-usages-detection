package upt.ac.cti.coverage.analysis.flow.insensitive.model;

public final class This extends BaseObject {
  private static final This instance = new This();

  public static This instance() {
    return instance;
  }

  @Override
  public boolean isThis() {
    return true;
  }

  @Override
  public String toString() {
    return "This";
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    return instance == obj;
  }

}

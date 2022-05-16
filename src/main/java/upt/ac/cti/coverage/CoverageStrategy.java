package upt.ac.cti.coverage;

public enum CoverageStrategy {
  FLOW_INSENSITIVE("flow_insensitive"), NAME_SIMILARITY("name_similarity");

  public final String name;

  private CoverageStrategy(String name) {
    this.name = name;
  }
}


package upt.ac.cti.coverage;

public enum CoverageStrategy {
  CONSERVING_FLOW_INSENSITIVE("CFI"), CONSERVING_NAME_SIMILARITY(
      "CNS"), SQUANDERING_FLOW_INSENSITIVE("SFI"), SQUANDERING_NAME_SIMILARITY("SNS");

  public final String name;

  private CoverageStrategy(String name) {
    this.name = name;
  }
}


package upt.ac.cti.coverage;

import java.util.function.Function;
import familypolymorphismdetection.metamodel.entity.MClass;

public enum CoverageStrategy {
  CONSERVING_FLOW_INSENSITIVE("CFI", MClass::cFI_ApertureCoverage),

  CONSERVING_NAME_SIMILARITY("CNS", MClass::cNS_ApertureCoverage),

  SQUANDERING_FLOW_INSENSITIVE("SFI", MClass::sFI_ApertureCoverage),

  SQUANDERING_NAME_SIMILARITY("SNS", MClass::sNS_ApertureCoverage);

  public final String name;

  public final Function<MClass, Double> apertureCoverage;

  private CoverageStrategy(String name, Function<MClass, Double> apertureCoverage) {
    this.name = name;
    this.apertureCoverage = apertureCoverage;
  }
}


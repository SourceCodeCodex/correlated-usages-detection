package upt.ac.cti.core.pair.field.property;

import java.util.function.Function;
import familypolymorphismdetection.metamodel.entity.MFieldPair;

abstract class ApertureCoverage {

  protected abstract Function<MFieldPair, Integer> coverage();

  public Double compute(MFieldPair mFieldPair) {
    var aperture = (double) mFieldPair.aperture();
    var coverage = (double) coverage().apply(mFieldPair);

    if (coverage < 0) {
      return coverage;
    }

    return coverage / aperture;
  }

}

package upt.ac.cti.core.pair.parameter.property;

import java.util.function.Function;
import familypolymorphismdetection.metamodel.entity.MParameterPair;

abstract class ApertureCoverage {

  protected abstract Function<MParameterPair, Integer> coverage();

  public Double compute(MParameterPair mParameterPair) {
    var aperture = (double) mParameterPair.aperture();
    var coverage = (double) coverage().apply(mParameterPair);

    if (coverage < 0) {
      return coverage;
    }

    return coverage / aperture;
  }

}

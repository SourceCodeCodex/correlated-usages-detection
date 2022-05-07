package upt.ac.cti.core.pair.parameter.property;

import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ApertureCoverage implements IPropertyComputer<Double, MParameterPair> {

  @Override
  public Double compute(MParameterPair mParameterPair) {
    var aperture = (double) mParameterPair.aperture();
    var coverage = (double) mParameterPair.coverage();

    if (coverage < 0) {
      return coverage;
    }

    return coverage / aperture;
  }

}

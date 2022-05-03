package upt.ac.cti.core.pair.parameter.property;

import java.util.logging.Logger;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ApertureCoverage implements IPropertyComputer<Double, MParameterPair> {

  private static final Logger logger = Logger.getLogger(ApertureCoverage.class.getName());

  @Override
  public Double compute(MParameterPair mParameterPair) {
    var aperture = (double) mParameterPair.aperture();
    var coverage = (double) mParameterPair.coverage();

    if (coverage == 0.0 || aperture == 0.0) {
      return Double.NaN;
    }

    logger.info(
        "Parameter Pair Aperture Coverage: " + mParameterPair.toString() + " = "
            + coverage / aperture);

    return coverage / aperture;
  }

}

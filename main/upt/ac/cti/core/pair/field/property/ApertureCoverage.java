package upt.ac.cti.core.pair.field.property;

import java.util.logging.Logger;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ApertureCoverage implements IPropertyComputer<Double, MFieldPair> {

  private static final Logger logger = Logger.getLogger(ApertureCoverage.class.getName());

  @Override
  public Double compute(MFieldPair mFieldPair) {
    var aperture = (double) mFieldPair.aperture();
    var coverage = (double) mFieldPair.coverage();

    if (coverage == 0.0 || aperture == 0.0) {
      return Double.NaN;
    }

    logger.info(
        "Field Pair Aperture Coverage: " + mFieldPair.toString() + " = " + coverage / aperture);

    return coverage / aperture;
  }

}

package upt.ac.cti.core.type.property;

import java.util.logging.Logger;
import familypolymorphismdetection.metamodel.entity.MClass;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.util.logging.RLogger;

@PropertyComputer
public final class ApertureCoverage implements IPropertyComputer<Double, MClass> {

  private static final Logger logger = RLogger.get();

  @Override
  public Double compute(MClass mClass) {
    var fieldApertureCoverage = mClass.fieldApertureCoverage();
    // var parameterApertureCoverage = mClass.parameterApertureCoverage();
    // var result = Double.min(fieldApertureCoverage, parameterApertureCoverage);
    var result = fieldApertureCoverage;
    logger.info("Class Aperture Coverage: " + mClass.toString() + ": " + result);
    return result;
  }

}

package upt.ac.cti.core.classes.property;

import java.util.logging.Logger;
import familypolymorphismdetection.metamodel.entity.MClass;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ApertureCoverage implements IPropertyComputer<Double, MClass> {

  private static final Logger logger = Logger.getLogger(ApertureCoverage.class.getName());

  @Override
  public Double compute(MClass mClass) {
    var fieldApertureCoverage = mClass.fieldApertureCoverage();
    // var parameterApertureCoverage = mClass.parameterApertureCoverage();
    // var result = Double.min(fieldApertureCoverage, parameterApertureCoverage);
    var result = fieldApertureCoverage;
    logger.info("Class Aperture Coverage: " + mClass.toString() + " = " + result);
    return result;
  }

}

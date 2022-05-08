package upt.ac.cti.core.type.property;

import java.util.logging.Logger;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.util.ApertureCoverageUtil;
import upt.ac.cti.util.logging.RLogger;
import upt.ac.cti.util.time.StopWatch;

@PropertyComputer
public final class ApertureCoverage implements IPropertyComputer<Double, MClass> {

  private static final Logger logger = RLogger.get();

  @Override
  public Double compute(MClass mClass) {
    var stopWatch = new StopWatch();

    stopWatch.start();
    logger.info("Start Aperture Coverage analysis: " + mClass.toString());

    var fieldApertureCoverage = fieldApertureCoverage(mClass);
    // var parameterApertureCoverage = mClass.parameterApertureCoverage();
    // var result = Double.min(fieldApertureCoverage, parameterApertureCoverage);
    var result = fieldApertureCoverage;
    stopWatch.stop();
    logger.info("Aperture Coverage (" + stopWatch.getDuration().toMillis() + "ms" + "): "
        + mClass.toString() + ": " + result);

    return result;
  }

  private Double fieldApertureCoverage(MClass mClass) {
    var apertureCoverages = mClass.susceptibleFieldPairs().getElements()
        .stream()
        .map(MFieldPair::apertureCoverage)
        .toList();

    return ApertureCoverageUtil.combine(apertureCoverages);
  }

  private Double parameterApertureCoverage(MClass mClass) {
    var apertureCoverages = mClass.susceptibleParameterPairs().getElements()
        .stream()
        .map(MParameterPair::apertureCoverage)
        .toList();

    return ApertureCoverageUtil.combine(apertureCoverages);
  }

}

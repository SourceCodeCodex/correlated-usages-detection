package upt.ac.cti.core.type.property;

import java.util.List;
import java.util.logging.Logger;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.util.computation.ApertureCoverageUtil;
import upt.ac.cti.util.logging.RLogger;
import upt.ac.cti.util.time.StopWatch;

@PropertyComputer
public final class ApertureCoverage implements IPropertyComputer<Double, MClass> {

  private static final Logger logger = RLogger.get();

  @Override
  public Double compute(MClass mClass) {
    var stopWatch = new StopWatch();

    stopWatch.start();

    var fieldApertureCoverage = fieldApertureCoverage(mClass);

    if (fieldApertureCoverage > 0 && fieldApertureCoverage < 1) {
      stopWatch.stop();
      logResult(fieldApertureCoverage, fieldApertureCoverage, Double.NaN, mClass, stopWatch);

      return fieldApertureCoverage;
    }

    var parameterApertureCoverage = parameterApertureCoverage(mClass);

    var apertureCoverage =
        ApertureCoverageUtil.combine(List.of(fieldApertureCoverage, parameterApertureCoverage));
    stopWatch.stop();

    logResult(apertureCoverage, fieldApertureCoverage, parameterApertureCoverage, mClass,
        stopWatch);
    return apertureCoverage;
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

  private void logResult(
      Double apertureCoverage,
      Double fieldApertureCoverage,
      Double parameterApertureCoverage,
      MClass mClass,
      StopWatch stopWatch) {
    var log = String.format("(\u001B[1mAC: %.10f\u001B[22m, F: %.7f, P: %.7f): %s in %d ms",
        apertureCoverage,
        fieldApertureCoverage,
        parameterApertureCoverage,
        mClass.toString(),
        stopWatch.getDuration().toMillis());
    logger.info(log);
  }

}

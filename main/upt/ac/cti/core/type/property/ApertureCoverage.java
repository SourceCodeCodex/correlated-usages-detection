package upt.ac.cti.core.type.property;

import java.util.List;
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
    // Writing's binding is inconclusive: Writing [element=meta, writingExpression=smi,
    // accessExpression=Left(DimensionLookup), depth=0]

    // Writing's binding is inconclusive: Writing [element=meta, writingExpression=smi,
    // accessExpression=Left(MergeJoin), depth=0]

    // Writing's binding is inconclusive: Writing [element=meta, writingExpression=smi,
    // accessExpression=Left(MonetDBBulkLoader), depth=0]

    // No derivation is possible for Writing [element=currentRowSet,
    // writingExpression=data.rowSets[0], accessExpression=Right(data), depth=0]. Writing expression
    // type is 2

    // Writing's binding is inconclusive: Writing [element=meta, writingExpression=smi,
    // accessExpression=Left(TableInput), depth=0]

    // Writing's binding is inconclusive: Writing [element=meta, writingExpression=smi,
    // accessExpression=Left(TableOutput), depth=0]

    // Writing's binding is inconclusive: Writing [element=data, writingExpression=sdi,
    // accessExpression=Left(TextFileOutput), depth=0]

    // Writing's binding is inconclusive: Writing [element=variablizedStepMeta,
    // writingExpression=stepMetaInterface, accessExpression=Left(BaseStreamStep), depth=0]

    // Writing's binding is inconclusive: Writing [element=variablizedStepMeta,
    // writingExpression=variablizedStepMeta.withVariables(this),
    // accessExpression=Left(BaseStreamStep), depth=0]

    var fieldApertureCoverage = fieldApertureCoverage(mClass);
    var parameterApertureCoverage = parameterApertureCoverage(mClass);

    var apertureCoverage =
        ApertureCoverageUtil.combine(List.of(fieldApertureCoverage, parameterApertureCoverage));
    stopWatch.stop();

    var log = String.format("(\u001B[1mAC: %.10f\u001B[22m, F: %.7f, P: %.7f): %s in %d ms",
        apertureCoverage,
        fieldApertureCoverage,
        parameterApertureCoverage,
        mClass.toString(),
        stopWatch.getDuration().toMillis());
    logger.info(log);

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

}

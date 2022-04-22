package upt.ac.cti.model.pair.field.property;

import java.util.logging.Logger;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ApertureCoverage implements IPropertyComputer<Double, MFieldPair> {

  private static final Logger logger = Logger.getLogger(ApertureCoverage.class.getSimpleName());

  @Override
  public Double compute(MFieldPair mFieldPair) {
    var result = ((double) mFieldPair.coverage()) / mFieldPair.aperture();

    var log = String.format("Property: %s - %s: %f", this.getClass().getName(), mFieldPair, result);
    logger.info(log);

    return result;
  }

}

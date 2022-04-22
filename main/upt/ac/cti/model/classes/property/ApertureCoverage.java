package upt.ac.cti.model.classes.property;

import java.util.logging.Logger;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ApertureCoverage implements IPropertyComputer<Double, MClass> {

  private static final Logger logger = Logger.getLogger(ApertureCoverage.class.getName());

  @Override
  public Double compute(MClass mClass) {
    var result = 1.0;
    for (MFieldPair p : mClass.fieldPairs().getElements()) {
      var ac = p.apertureCoverage();
      if (ac < result) {
        result = ac;
      }
    }

    var log = String.format("Property: %s - %s: %f", this.getClass().getName(), mClass, result);
    logger.info(log);

    return result;
  }

}

package upt.ac.cti.model.pair.field.property;

import java.util.logging.Logger;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public class Aperture implements IPropertyComputer<Integer, MFieldPair> {

  private static final Logger logger = Logger.getLogger(Aperture.class.getSimpleName());

  @Override
  public Integer compute(MFieldPair mFieldPair) {
    var result = mFieldPair.typePairs().getElements().size();

    var log = String.format("Property: %s - %s: %f", this.getClass().getName(), mFieldPair, result);
    logger.info(log);

    return result;
  }

}

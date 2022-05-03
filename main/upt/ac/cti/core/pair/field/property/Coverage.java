package upt.ac.cti.core.pair.field.property;

import java.util.logging.Logger;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class Coverage implements IPropertyComputer<Integer, MFieldPair> {

  private static final Logger logger = Logger.getLogger(Aperture.class.getName());

  @Override
  public Integer compute(MFieldPair mFieldPair) {
    var result = mFieldPair.coveredTypePairs().getElements().size();
    logger.info("Field Pair Coverage: " + mFieldPair.toString() + " = " + result);
    return result;
  }

}

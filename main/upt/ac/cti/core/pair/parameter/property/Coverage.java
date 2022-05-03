package upt.ac.cti.core.pair.parameter.property;

import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class Coverage implements IPropertyComputer<Integer, MParameterPair> {

  @Override
  public Integer compute(MParameterPair mParameterPair) {
    return mParameterPair.coveredTypePairs().getElements().size();
  }

}

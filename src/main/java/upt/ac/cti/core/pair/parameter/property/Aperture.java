package upt.ac.cti.core.pair.parameter.property;

import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class Aperture implements IPropertyComputer<Integer, MParameterPair> {

  @Override
  public Integer compute(MParameterPair mParameterPair) {
    var result = mParameterPair.allTypePairs().getElements().size();
    return result;
  }

}

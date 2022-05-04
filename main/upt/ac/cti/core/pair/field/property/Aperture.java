package upt.ac.cti.core.pair.field.property;

import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class Aperture implements IPropertyComputer<Integer, MFieldPair> {

  @Override
  public Integer compute(MFieldPair mFieldPair) {
    var result = mFieldPair.allTypePairs().getElements().size();
    return result;
  }

}

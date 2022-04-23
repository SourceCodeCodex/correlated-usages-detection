package upt.ac.cti.model.pair.field.property;

import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class Coverage implements IPropertyComputer<Integer, MFieldPair> {

  @Override
  public Integer compute(MFieldPair fieldPair) {
    return fieldPair.coveredTypePairs().getElements().size();
  }

}

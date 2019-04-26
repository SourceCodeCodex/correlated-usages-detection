package upt.se.arguments.tuple;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MTypePair;

@PropertyComputer
public class Apperture implements IPropertyComputer<Double, MTypePair> {

  @Override
  public Double compute(MTypePair entity) {
    return entity.usedArgumentsTypes().getElements().size() * 100d
        / entity.allArgumentsTypes().getElements().size();
  }

}

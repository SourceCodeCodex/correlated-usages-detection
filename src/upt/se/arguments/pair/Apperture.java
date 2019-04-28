package upt.se.arguments.pair;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MParameterPair;

@PropertyComputer
public class Apperture implements IPropertyComputer<Double, MParameterPair> {

  @Override
  public Double compute(MParameterPair entity) {
    return entity.usedArgumentsTypes().getElements().size() * 100d
        / entity.allArgumentsTypes().getElements().size();
  }

}

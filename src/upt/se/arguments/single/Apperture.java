package upt.se.arguments.single;

import static upt.se.utils.helpers.Converter.round;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MArgumentType;

@PropertyComputer
public class Apperture implements IPropertyComputer<String, MArgumentType> {

  @Override
  public String compute(MArgumentType entity) {
    double apperture = entity.usedArgumentTypes().getElements().size() * 100d
        / entity.allArgumentTypes().getElements().size();

    return round(apperture, 2) + " %";
  }

}

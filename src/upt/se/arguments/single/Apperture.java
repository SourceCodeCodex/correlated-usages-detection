package upt.se.arguments.single;

import static upt.se.utils.helpers.Converter.round;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MParameter;

@PropertyComputer
public class Apperture implements IPropertyComputer<String, MParameter> {

  @Override
  public String compute(MParameter entity) {
    double apperture = entity.usedArgumentTypes().getElements().size() * 100d
        / entity.allPossibleArgumentTypes().getElements().size();

    return round(apperture, 2) + " %";
  }

}

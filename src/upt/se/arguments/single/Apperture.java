package upt.se.arguments.single;

import static upt.se.utils.helpers.Converter.round;
import static upt.se.utils.helpers.Equals.*;
import java.util.List;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MParameter;

@PropertyComputer
public class Apperture implements IPropertyComputer<String, MParameter> {

  @Override
  public String compute(MParameter entity) {
    List<MClass> usedTypes = entity.usedArgumentTypes().getElements();

    if (usedTypes.stream().anyMatch(
        usedType -> isEqual(usedType.getUnderlyingObject(), entity.getUnderlyingObject().getSuperclass()))) {
      return 100 + "%";
    }
    
    double apperture = usedTypes.size() * 100d
        / entity.allPossibleArgumentTypes().getElements().size();

    return round(apperture, 2) + " %";
  }

}

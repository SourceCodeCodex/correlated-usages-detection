package upt.se.parameters;

import static upt.se.utils.helpers.ClassNames.getName;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MArgumentType;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MArgumentType> {

  @Override
  public String compute(MArgumentType entity) {
    return getName(entity);
  }

}

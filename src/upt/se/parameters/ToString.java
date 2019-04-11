package upt.se.parameters;

import static upt.se.utils.helpers.ClassNames.getName;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MTypeParameter;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MTypeParameter> {

  @Override
  public String compute(MTypeParameter entity) {
    return getName(entity);
  }

}

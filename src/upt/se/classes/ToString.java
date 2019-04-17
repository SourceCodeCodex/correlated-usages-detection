package upt.se.classes;

import static upt.se.utils.helpers.ClassNames.getName;
import static upt.se.utils.helpers.ClassNames.isObject;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MClass;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MClass> {

  @Override
  public String compute(MClass entity) {
    if (isObject(entity)) {
      return "*";
    }
    return getName(entity);
  }

}

package upt.se.classes;

import static upt.se.utils.helpers.Equals.isObject;
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
    return entity.getUnderlyingObject().getElementName();
  }

}

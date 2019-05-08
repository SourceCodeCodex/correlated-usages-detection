package upt.se.arguments.single;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MClass;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MClass> {

  @Override
  public String compute(MClass entity) {
    return entity.getUnderlyingObject().getFullyQualifiedName();
  }

}

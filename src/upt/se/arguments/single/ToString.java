package upt.se.arguments.single;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MArgument;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MArgument> {

  @Override
  public String compute(MArgument entity) {
    return entity.getUnderlyingObject().getFullyQualifiedName();
  }

}

package upt.se.arguments.single;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MArgumentType;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MArgumentType> {

  @Override
  public String compute(MArgumentType entity) {
    return entity.getUnderlyingObject().getJavaElement().getElementName();
  }

}

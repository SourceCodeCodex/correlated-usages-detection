package upt.se.parameters.single;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MParameter;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MParameter> {

  @Override
  public String compute(MParameter entity) {
    return entity.getUnderlyingObject().getQualifiedName();
  }

}

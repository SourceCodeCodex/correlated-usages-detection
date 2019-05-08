package upt.se.arguments.pair;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MClassPair;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MClassPair> {

  @Override
  public String compute(MClassPair entity) {
    String first = entity.getUnderlyingObject().getFirst().getFullyQualifiedName();
    String second = entity.getUnderlyingObject().getSecond().getFullyQualifiedName();

    return "(" + first + ", " + second + ")";
  }


}

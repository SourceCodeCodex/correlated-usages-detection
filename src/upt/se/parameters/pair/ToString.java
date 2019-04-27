package upt.se.parameters.pair;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MParameterPair;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MParameterPair> {

  @Override
  public String compute(MParameterPair entity) {
    String first = entity.getUnderlyingObject().getFirst().getQualifiedName();
    String second = entity.getUnderlyingObject().getSecond().getQualifiedName();

    return "(" + first + ", " + second + ")";
  }

}

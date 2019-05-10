package upt.se.arguments.pair;

import java.util.List;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MClassPair;
import thesis.metamodel.entity.MParameterPair;

@PropertyComputer
public class Apperture implements IPropertyComputer<Double, MParameterPair> {

  @Override
  public Double compute(MParameterPair entity) {
    List<MClassPair> usedTypes = entity.usedArgumentsTypes().getElements();
    List<MClassPair> allTypes = entity.allPossibleArgumentsTypes().getElements();
    return usedTypes.size() * 100d
        / allTypes.size();
  }

}

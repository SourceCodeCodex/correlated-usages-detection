package upt.ac.cti.core.pair.parameter.property;

import org.eclipse.jdt.core.ILocalVariable;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ToString implements IPropertyComputer<String, MParameterPair> {

  @Override
  public String compute(MParameterPair mParameterPair) {
    @SuppressWarnings("unchecked")
    var iPair = (Pair<ILocalVariable, ILocalVariable>) mParameterPair.getUnderlyingObject();
    var _1 = iPair.getValue0();
    var _2 = iPair.getValue1();
    return String.format("(%s - %s, %s - %s)",
        _1.getDeclaringMember().getElementName(),
        _1.getElementName(),
        _2.getDeclaringMember().getElementName(),
        _2.getElementName());
  }

}

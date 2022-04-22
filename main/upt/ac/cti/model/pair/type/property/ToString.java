package upt.ac.cti.model.pair.type.property;

import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ToString implements IPropertyComputer<String, MTypePair> {

  @Override
  public String compute(MTypePair mTypePair) {
    @SuppressWarnings("unchecked")
    var iPair = (Pair<IType, IType>) mTypePair.getUnderlyingObject();
    var _1 = iPair.getValue0();
    var _2 = iPair.getValue1();
    return String.format("(%s, %s)", _1.getFullyQualifiedName(), _2.getFullyQualifiedName());
  }

}

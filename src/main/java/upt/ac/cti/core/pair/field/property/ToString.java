package upt.ac.cti.core.pair.field.property;

import org.eclipse.jdt.core.IField;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ToString implements IPropertyComputer<String, MFieldPair> {

  @Override
  public String compute(MFieldPair mFieldPair) {
    @SuppressWarnings("unchecked")
    var iPair = (Pair<IField, IField>) mFieldPair.getUnderlyingObject();
    var _1 = iPair.getValue0();
    var _2 = iPair.getValue1();
    return String.format("(%s, %s)", _1.getElementName(), _2.getElementName());
  }

}

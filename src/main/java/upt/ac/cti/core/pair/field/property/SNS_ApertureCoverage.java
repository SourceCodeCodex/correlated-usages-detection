package upt.ac.cti.core.pair.field.property;

import java.util.function.Function;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class SNS_ApertureCoverage extends ApertureCoverage
    implements IPropertyComputer<Double, MFieldPair> {

  @Override
  protected Function<MFieldPair, Integer> coverage() {
    return MFieldPair::sNS_Coverage;
  }


}

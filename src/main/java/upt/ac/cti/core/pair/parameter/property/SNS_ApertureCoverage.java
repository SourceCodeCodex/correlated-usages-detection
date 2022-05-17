package upt.ac.cti.core.pair.parameter.property;

import java.util.function.Function;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class SNS_ApertureCoverage extends ApertureCoverage
    implements IPropertyComputer<Double, MParameterPair> {

  @Override
  protected Function<MParameterPair, Integer> coverage() {
    return MParameterPair::sNS_Coverage;
  }


}

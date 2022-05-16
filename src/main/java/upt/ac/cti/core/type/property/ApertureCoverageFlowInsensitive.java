package upt.ac.cti.core.type.property;

import java.util.function.Function;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ApertureCoverageFlowInsensitive extends ApertureCoverage
    implements IPropertyComputer<Double, MClass> {

  @Override
  protected Function<MFieldPair, Double> fieldPairApertureCoverage() {
    return MFieldPair::apertureCoverageFlowInsensitive;
  }

  @Override
  protected Function<MParameterPair, Double> parameterPairApertureCoverage() {
    return MParameterPair::apertureCoverageFlowInsensitive;
  }

}

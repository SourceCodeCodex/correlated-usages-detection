package upt.ac.cti.core.type.property;

import java.util.function.Function;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.coverage.CoverageStrategy;

@PropertyComputer
public final class CNS_ApertureCoverage extends ApertureCoverage
    implements IPropertyComputer<Double, MClass> {

  public CNS_ApertureCoverage() {
    super(CoverageStrategy.CONSERVING_NAME_SIMILARITY);
  }

  @Override
  protected Function<MFieldPair, Double> fieldPairApertureCoverage() {
    return MFieldPair::cNS_ApertureCoverage;
  }

  @Override
  protected Function<MParameterPair, Double> parameterPairApertureCoverage() {
    return MParameterPair::cNS_ApertureCoverage;
  }

}

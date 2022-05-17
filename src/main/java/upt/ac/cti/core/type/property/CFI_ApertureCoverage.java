package upt.ac.cti.core.type.property;

import java.util.function.Function;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.coverage.CoverageStrategy;

@PropertyComputer
public final class CFI_ApertureCoverage extends ApertureCoverage
    implements IPropertyComputer<Double, MClass> {

  public CFI_ApertureCoverage() {
    super(CoverageStrategy.CONSERVING_FLOW_INSENSITIVE);
  }

  @Override
  protected Function<MFieldPair, Double> fieldPairApertureCoverage() {
    return MFieldPair::cFI_ApertureCoverage;
  }

  @Override
  protected Function<MParameterPair, Double> parameterPairApertureCoverage() {
    return MParameterPair::cFI_ApertureCoverage;
  }

}

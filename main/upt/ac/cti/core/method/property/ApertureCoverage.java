package upt.ac.cti.core.method.property;

import familypolymorphismdetection.metamodel.entity.MMethod;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.util.ApertureCoverageUtil;

@PropertyComputer
public final class ApertureCoverage implements IPropertyComputer<Double, MMethod> {

  @Override
  public Double compute(MMethod mMethod) {
    var apertureCoverages = mMethod.susceptibleParameterPairs().getElements()
        .stream()
        .map(MParameterPair::apertureCoverage)
        .toList();

    return ApertureCoverageUtil.combine(apertureCoverages);
  }

}

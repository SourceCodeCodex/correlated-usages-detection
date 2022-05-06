package upt.ac.cti.core.method.property;

import java.util.Comparator;
import familypolymorphismdetection.metamodel.entity.MMethod;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ApertureCoverage implements IPropertyComputer<Double, MMethod> {

  @Override
  public Double compute(MMethod mMethod) {
    return mMethod.susceptibleParameterPairs().getElements()
        .stream()
        .map(MParameterPair::apertureCoverage)
        .filter(d -> !d.isNaN())
        .min(Comparator.naturalOrder())
        .orElse(Double.NaN);
  }

}

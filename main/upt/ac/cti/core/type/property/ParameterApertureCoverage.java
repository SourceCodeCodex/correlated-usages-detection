package upt.ac.cti.core.type.property;

import java.util.Comparator;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MMethod;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ParameterApertureCoverage implements IPropertyComputer<Double, MClass> {

  @Override
  public Double compute(MClass mClass) {
    return mClass.methods().getElements()
        .stream()
        .map(MMethod::apertureCoverage)
        .filter(d -> !d.isNaN())
        .min(Comparator.naturalOrder())
        .orElse(Double.NaN);
  }

}

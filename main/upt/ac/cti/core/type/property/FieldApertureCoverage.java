package upt.ac.cti.core.type.property;

import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.util.ApertureCoverageUtil;

@PropertyComputer
public final class FieldApertureCoverage implements IPropertyComputer<Double, MClass> {

  @Override
  public Double compute(MClass mClass) {
    var apertureCoverages = mClass.susceptibleFieldPairs().getElements()
        .stream()
        .map(MFieldPair::apertureCoverage)
        .toList();

    return ApertureCoverageUtil.combine(apertureCoverages);
  }

}

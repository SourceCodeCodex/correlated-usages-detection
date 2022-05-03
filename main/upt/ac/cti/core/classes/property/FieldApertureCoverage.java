package upt.ac.cti.core.classes.property;

import java.util.Comparator;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class FieldApertureCoverage implements IPropertyComputer<Double, MClass> {

  @Override
  public Double compute(MClass mClass) {
    return mClass.fieldPairs().getElements()
        .stream()
        .map(MFieldPair::apertureCoverage)
        .filter(d -> !d.isNaN())
        .min(Comparator.naturalOrder())
        .orElse(Double.NaN);
  }

}

package upt.ac.cti.core.pair.field.property;

import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ApertureCoverage implements IPropertyComputer<Double, MFieldPair> {

  @Override
  public Double compute(MFieldPair mFieldPair) {
    var aperture = (double) mFieldPair.aperture();
    var coverage = (double) mFieldPair.coverage();

    if (coverage < 0) {
      return coverage;
    }

    return coverage / aperture;
  }

}

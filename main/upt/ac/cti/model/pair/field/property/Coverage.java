package upt.ac.cti.model.pair.field.property;

import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.analysis.coverage.flow.insensitive.CoverageAnalysis;

@PropertyComputer
public final class Coverage implements IPropertyComputer<Integer, MFieldPair> {

  @Override
  public Integer compute(MFieldPair mFieldPair) {
    var analysis = new CoverageAnalysis(mFieldPair);

    var result = analysis.coverage();

    return result;
  }

}

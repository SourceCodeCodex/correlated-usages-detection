package upt.ac.cti.model.pair.field.property;

import java.util.logging.Logger;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.analysis.coverage.flow.insensitive.CoverageAnalysis;

@PropertyComputer
public class Coverage implements IPropertyComputer<Integer, MFieldPair> {

  private static final Logger logger = Logger.getLogger(Coverage.class.getSimpleName());

  @Override
  public Integer compute(MFieldPair mFieldPair) {
    var analysis = new CoverageAnalysis(mFieldPair);

    var result = analysis.coverage();

    var log = String.format("Property: %s - %s: %f", this.getClass().getName(), mFieldPair, result);
    logger.info(log);

    return result;
  }

}

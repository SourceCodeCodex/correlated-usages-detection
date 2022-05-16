package upt.ac.cti.core.project.action;

import java.util.function.Function;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MProject;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;
import upt.ac.cti.coverage.CoverageStrategy;

@ActionPerformer
public class ExportReportFlowInsensitive extends ExportReport
    implements IActionPerformer<Void, MProject, HListEmpty> {

  @Override
  protected Function<MClass, Double> apertureCoverage() {
    return MClass::apertureCoverageFlowInsensitive;
  }

  @Override
  protected CoverageStrategy strategyName() {
    return CoverageStrategy.FLOW_INSENSITIVE;
  }

}

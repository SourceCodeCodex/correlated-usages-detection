package upt.ac.cti.core.project.action;

import java.util.Set;
import familypolymorphismdetection.metamodel.entity.MProject;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;
import upt.ac.cti.coverage.CoverageStrategy;

@ActionPerformer
public class AllStrategies_ExportReport extends ExportReport
    implements IActionPerformer<Void, MProject, HListEmpty> {

  @Override
  protected Set<CoverageStrategy> strategies() {
    return Set.of(
        CoverageStrategy.CONSERVING_FLOW_INSENSITIVE,
        CoverageStrategy.SQUANDERING_FLOW_INSENSITIVE,
        CoverageStrategy.CONSERVING_NAME_SIMILARITY,
        CoverageStrategy.SQUANDERING_NAME_SIMILARITY);
  }

}

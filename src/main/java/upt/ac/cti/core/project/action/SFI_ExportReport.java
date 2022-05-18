package upt.ac.cti.core.project.action;

import java.util.Set;
import familypolymorphismdetection.metamodel.entity.MProject;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;
import upt.ac.cti.coverage.CoverageStrategy;

@ActionPerformer
public class SFI_ExportReport extends ExportReport
    implements IActionPerformer<Void, MProject, HListEmpty> {

  @Override
  protected Set<CoverageStrategy> strategies() {
    return Set.of(CoverageStrategy.SQUANDERING_FLOW_INSENSITIVE);
  }

}

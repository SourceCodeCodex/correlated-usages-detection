package upt.ac.cti.core.workingset.action;

import java.util.function.Consumer;
import familypolymorphismdetection.metamodel.entity.MProject;
import familypolymorphismdetection.metamodel.entity.MWorkingSet;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;

@ActionPerformer
public class SNS_ExportReports extends ExportReports
    implements IActionPerformer<Void, MWorkingSet, HListEmpty> {

  @Override
  protected Consumer<MProject> exportReport() {
    return MProject::sNS_ExportReport;
  }


}

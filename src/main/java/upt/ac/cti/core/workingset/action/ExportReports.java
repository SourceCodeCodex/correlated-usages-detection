package upt.ac.cti.core.workingset.action;

import java.util.function.Consumer;
import familypolymorphismdetection.metamodel.entity.MProject;
import familypolymorphismdetection.metamodel.entity.MWorkingSet;
import ro.lrg.xcore.metametamodel.HListEmpty;

abstract class ExportReports {

  protected abstract Consumer<MProject> exportReport();

  public Void performAction(MWorkingSet mWorkingSet, HListEmpty arg1) {
    mWorkingSet.javaProjects().getElements().forEach(exportReport());
    return null;
  }

}

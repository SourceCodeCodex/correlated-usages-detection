package upt.ac.cti.core.workingset.action;

import familypolymorphismdetection.metamodel.entity.MProject;
import familypolymorphismdetection.metamodel.entity.MWorkingSet;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;

@ActionPerformer
public class ExportReports implements IActionPerformer<Void, MWorkingSet, HListEmpty> {

  @Override
  public Void performAction(MWorkingSet mWorkingSet, HListEmpty arg1) {

    mWorkingSet.javaProjects().getElements().forEach(MProject::exportReport);

    return null;
  }

}

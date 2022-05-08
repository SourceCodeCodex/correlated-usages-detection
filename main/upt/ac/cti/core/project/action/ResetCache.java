package upt.ac.cti.core.project.action;

import familypolymorphismdetection.metamodel.entity.MProject;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;
import upt.ac.cti.dependency.Dependencies;

@ActionPerformer
public final class ResetCache implements IActionPerformer<Void, MProject, HListEmpty> {

  @Override
  public Void performAction(MProject mProject, HListEmpty hList) {
    Dependencies.init();
    return null;
  }

}

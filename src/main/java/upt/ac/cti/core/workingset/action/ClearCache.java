package upt.ac.cti.core.workingset.action;

import familypolymorphismdetection.metamodel.entity.MWorkingSet;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;
import upt.ac.cti.dependency.Dependencies;

@ActionPerformer
public final class ClearCache implements IActionPerformer<Void, MWorkingSet, HListEmpty> {

  @Override
  public Void performAction(MWorkingSet mWorkingSet, HListEmpty hList) {
    Dependencies.init();
    return null;
  }

}

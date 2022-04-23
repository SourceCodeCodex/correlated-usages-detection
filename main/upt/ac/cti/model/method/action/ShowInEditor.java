package upt.ac.cti.model.method.action;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.PartInitException;
import familypolymorphismdetection.metamodel.entity.MMethod;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;

@ActionPerformer
public final class ShowInEditor implements IActionPerformer<Void, MMethod, HListEmpty> {

  @Override
  public Void performAction(MMethod mMethod, HListEmpty hList) {
    try {
      JavaUI.openInEditor((IMethod) mMethod.getUnderlyingObject(), true, true);
    } catch (PartInitException | JavaModelException e) {
      e.printStackTrace();
    }
    return null;
  }

}

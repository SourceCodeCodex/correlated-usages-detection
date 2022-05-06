package upt.ac.cti.core.type.action;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.PartInitException;
import familypolymorphismdetection.metamodel.entity.MClass;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;

@ActionPerformer
public final class ShowInEditor implements IActionPerformer<Void, MClass, HListEmpty> {

  @Override
  public Void performAction(MClass mClass, HListEmpty hList) {
    try {
      JavaUI.openInEditor((IType) mClass.getUnderlyingObject(), true, true);
    } catch (PartInitException | JavaModelException e) {
      e.printStackTrace();
    }
    return null;
  }

}

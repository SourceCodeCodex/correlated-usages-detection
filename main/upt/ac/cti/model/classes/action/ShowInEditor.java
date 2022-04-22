package upt.ac.cti.model.classes.action;

import java.util.logging.Logger;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.PartInitException;
import familypolymorphismdetection.metamodel.entity.MClass;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;

@ActionPerformer
public class ShowInEditor implements IActionPerformer<Void, MClass, HListEmpty> {

  private static final Logger logger = Logger.getLogger(ShowInEditor.class.getName());

  @Override
  public Void performAction(MClass mClass, HListEmpty hList) {
    logger.info(String.format("Action: %s - %s", this.getClass().getName(), mClass));

    try {
      JavaUI.openInEditor((IType) mClass.getUnderlyingObject(), true, true);
    } catch (PartInitException | JavaModelException e) {
      var ste = e.getStackTrace()[0];
      logger.throwing(ste.getClassName(), ste.getMethodName(), e);
    }
    return null;
  }

}

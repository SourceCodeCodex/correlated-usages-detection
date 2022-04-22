package upt.ac.cti.model.method.action;

import java.util.logging.Logger;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.PartInitException;
import familypolymorphismdetection.metamodel.entity.MMethod;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;

@ActionPerformer
public class ShowInEditor implements IActionPerformer<Void, MMethod, HListEmpty> {

  private static final Logger logger = Logger.getLogger(ShowInEditor.class.getSimpleName());

  @Override
  public Void performAction(MMethod mMethod, HListEmpty hList) {
    logger.info(String.format("Action: %s - %s", this.getClass().getName(), mMethod));

    try {
      JavaUI.openInEditor((IMethod) mMethod.getUnderlyingObject(), true, true);
    } catch (PartInitException | JavaModelException e) {
      var ste = e.getStackTrace()[0];
      logger.throwing(ste.getClassName(), ste.getMethodName(), e);
    }
    return null;
  }

}

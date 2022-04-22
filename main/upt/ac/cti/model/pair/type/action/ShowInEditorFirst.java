package upt.ac.cti.model.pair.type.action;

import java.util.logging.Logger;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.PartInitException;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MTypePair;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;

@ActionPerformer
public class ShowInEditorFirst implements IActionPerformer<Void, MTypePair, HListEmpty> {

  private static final Logger logger = Logger.getLogger(ShowInEditorFirst.class.getSimpleName());

  @Override
  public Void performAction(MTypePair mTypePair, HListEmpty hList) {
    logger.info(String.format("Action: %s - %s", this.getClass().getName(), mTypePair));

    try {
      @SuppressWarnings("unchecked")
      var pair = (Pair<IType, IType>) mTypePair.getUnderlyingObject();
      JavaUI.openInEditor(pair.getValue0(), true, true);
    } catch (PartInitException | JavaModelException e) {
      var ste = e.getStackTrace()[0];
      logger.throwing(ste.getClassName(), ste.getMethodName(), e);
    }
    return null;
  }

}

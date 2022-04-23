package upt.ac.cti.model.pair.type.action;

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
public final class ShowInEditorSecond implements IActionPerformer<Void, MTypePair, HListEmpty> {

  @Override
  public Void performAction(MTypePair mTypePair, HListEmpty hList) {
    try {
      @SuppressWarnings("unchecked")
      var pair = (Pair<IType, IType>) mTypePair.getUnderlyingObject();
      JavaUI.openInEditor(pair.getValue1(), true, true);
    } catch (PartInitException | JavaModelException e) {
      e.printStackTrace();
    }
    return null;
  }

}

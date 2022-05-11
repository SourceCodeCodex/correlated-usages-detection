package upt.ac.cti.core.pair.parameter.action;

import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.PartInitException;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;

@ActionPerformer
public final class ShowInEditorSecond implements IActionPerformer<Void, MParameterPair, HListEmpty> {

  @Override
  public Void performAction(MParameterPair mParamterPair, HListEmpty hList) {
    try {
      @SuppressWarnings("unchecked")
      var pair = (Pair<ILocalVariable, ILocalVariable>) mParamterPair.getUnderlyingObject();
      JavaUI.openInEditor(pair.getValue1(), true, true);
    } catch (PartInitException | JavaModelException e) {
      e.printStackTrace();
    }
    return null;
  }

}

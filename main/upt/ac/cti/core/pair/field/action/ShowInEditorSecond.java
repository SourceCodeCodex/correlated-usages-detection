package upt.ac.cti.core.pair.field.action;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.PartInitException;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;

@ActionPerformer
public final class ShowInEditorSecond implements IActionPerformer<Void, MFieldPair, HListEmpty> {

  @Override
  public Void performAction(MFieldPair mFieldPair, HListEmpty hList) {
    try {
      @SuppressWarnings("unchecked")
      var pair = (Pair<IField, IField>) mFieldPair.getUnderlyingObject();
      JavaUI.openInEditor(pair.getValue1(), true, true);
    } catch (PartInitException | JavaModelException e) {
      e.printStackTrace();
    }
    return null;
  }

}

package upt.ac.cti.methods;

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

	@Override
	public Void performAction(MMethod mClass, HListEmpty hList) {
		try {
			JavaUI.openInEditor((IMethod) mClass.getUnderlyingObject(), true, true);
		} catch (PartInitException e) {
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

}
package upt.ac.cti.fields;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.PartInitException;

import familypolymorphismdetection.metamodel.entity.MField;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;

@ActionPerformer
public class ShowInEditor implements IActionPerformer<Void, MField, HListEmpty> {

	@Override
	public Void performAction(MField mField, HListEmpty hList) {
		try {
			JavaUI.openInEditor((IField) mField.getUnderlyingObject(), true, true);
		} catch (PartInitException e) {
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

}
package upt.ac.cti.ui;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.ui.IStartup;

import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.insider.view.ToolRegistration;
import upt.ac.cti.model.TypePair;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		ToolRegistration.getInstance().registerXEntityConverter(element -> {
			if (element instanceof IType) {
				return Factory.getInstance().createMClass(element);
			}
			if (element instanceof IField) {
				return Factory.getInstance().createMField(element);
			}
			if (element instanceof TypePair) {
				return Factory.getInstance().createMTypePair(element);
			}
			return null;
		});
	}

}
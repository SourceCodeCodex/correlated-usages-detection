package upt.ac.cti.methods;

import org.eclipse.jdt.core.IMethod;

import familypolymorphismdetection.metamodel.entity.MMethod;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MMethod> {

	@Override
	public String compute(MMethod mClass) {
		var iMethod = (IMethod) mClass.getUnderlyingObject();
		return iMethod.getElementName();
	}

}
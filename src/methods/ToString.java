package methods;

import org.eclipse.jdt.core.IMethod;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MMethod;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MMethod> {

	@Override
	public String compute(MMethod arg0) {
		IMethod m = arg0.getUnderlyingObject();
		return m.getElementName();
	}

}

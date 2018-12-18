package upt.se.classes;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MClass;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MClass> {

	@Override
	public String compute(MClass entity) {
		return entity.getUnderlyingObject().getElementName();
	}

}

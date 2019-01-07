package upt.se.classes;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MClass;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MClass> {

	@Override
	public String compute(MClass entity) {
		if (entity.getUnderlyingObject().getElementName().equals(Object.class.getSimpleName())) {
			return "*";
		}
		return entity.getUnderlyingObject().getElementName();
	}

}

package upt.ac.cti.fields;

import org.eclipse.jdt.core.IField;

import familypolymorphismdetection.metamodel.entity.MField;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MField> {

	@Override
	public String compute(MField mField) {
		var iField = (IField) mField.getUnderlyingObject();

		return iField.getElementName();

	}

}
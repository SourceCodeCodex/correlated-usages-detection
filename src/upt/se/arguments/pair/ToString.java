package upt.se.arguments.pair;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MTypePair;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MTypePair> {

	@Override
	public String compute(MTypePair entity) {
		String first = entity.getUnderlyingObject().getFirst().getJavaElement().getElementName();
		String second = entity.getUnderlyingObject().getSecond().getJavaElement().getElementName();

		return "(" + first + ", " + second + ")";
	}

	
}

package upt.se.parameters.single;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MParameter;

@PropertyComputer
public class IsBounded implements IPropertyComputer<Boolean, MParameter>{

	@Override
	public Boolean compute(MParameter entity) {
		return entity.getUnderlyingObject().getTypeBounds().length > 0;
	}

}

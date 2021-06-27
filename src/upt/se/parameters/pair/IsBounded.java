package upt.se.parameters.pair;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MParameterPair;

@PropertyComputer
public class IsBounded implements IPropertyComputer<Boolean, MParameterPair>{

	@Override
	public Boolean compute(MParameterPair entity) {
		return entity.getUnderlyingObject().getFirst().getTypeBounds().length > 0
				&& entity.getUnderlyingObject().getSecond().getTypeBounds().length > 0;
	}

}

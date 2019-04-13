package upt.se.parameters;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MTypeParameter;

@PropertyComputer
public class Apperture implements IPropertyComputer<Double, MTypeParameter> {

	@Override
	public Double compute(MTypeParameter entity) {
		return entity.usedParameterTypes().getElements().size() * 100d
				/ entity.allParameterTypes().getElements().size();
	}

}

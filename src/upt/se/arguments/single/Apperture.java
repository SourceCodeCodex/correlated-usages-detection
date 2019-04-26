package upt.se.arguments.single;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MArgumentType;

@PropertyComputer
public class Apperture implements IPropertyComputer<Double, MArgumentType> {

	@Override
	public Double compute(MArgumentType entity) {
		return entity.usedArgumentTypes().getElements().size() * 100d
				/ entity.allArgumentTypes().getElements().size();
	}

}

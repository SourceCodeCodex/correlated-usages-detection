package upt.ac.cti.field.pairs;

import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public class FieldPairAperture implements IPropertyComputer<Integer, MFieldPair> {

	@Override
	public Integer compute(MFieldPair mFieldPair) {
		return mFieldPair.possibleTypePairsGroup().getElements().size();
	}

}

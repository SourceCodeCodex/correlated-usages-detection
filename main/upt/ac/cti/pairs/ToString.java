package upt.ac.cti.pairs;

import familypolymorphismdetection.metamodel.entity.MTypePair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.model.TypePair;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MTypePair> {

	@Override
	public String compute(MTypePair mTypePair) {
		var typePair = (TypePair) mTypePair.getUnderlyingObject();
		return typePair.toString();
	}

}
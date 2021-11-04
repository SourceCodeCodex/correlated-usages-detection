package upt.ac.cti.classes;

import familypolymorphismdetection.metamodel.entity.MClass;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public class Aperture implements IPropertyComputer<Integer, MClass> {

	@Override
	public Integer compute(MClass mClass) {
		return mClass.typePairGroup().getElements().size();
	}

}
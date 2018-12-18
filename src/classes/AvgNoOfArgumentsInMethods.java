package classes;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MMethod;

@PropertyComputer
public class AvgNoOfArgumentsInMethods implements IPropertyComputer<Double, MClass> {

	@Override
	public Double compute(MClass arg0) {
		Group<MMethod> allMet = arg0.methodGroup();
		double all = 0;
		for (MMethod aMet : allMet.getElements()) {
			all += aMet.noOfArguments();
		}
		return all / allMet.getElements().size();
	}

}

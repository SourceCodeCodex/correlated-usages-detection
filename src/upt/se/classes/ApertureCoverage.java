package upt.se.classes;

import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MClass;

@PropertyComputer
public class ApertureCoverage implements IPropertyComputer<Double, MClass> {

	@Override
	public Double compute(MClass entity) {
		List<Double> apertures = List.ofAll(entity.typeParameterPairs().getElements())
	        .map(parameter -> parameter.aperture());
		if (apertures.size() == 0) {
			return Double.NaN;
		}
	    return apertures.fold(0d, (elem, acc) -> elem + acc) / apertures.size();
	}
	
}

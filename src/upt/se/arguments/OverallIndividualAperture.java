package upt.se.arguments;

import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MClass;

@PropertyComputer
public class OverallIndividualAperture implements IPropertyComputer<Double, MClass> {

  @Override
  public Double compute(MClass entity) {
    List<Double> apertures = List.ofAll(entity.typeParameterPairs().getElements())
        .map(parameter -> parameter.aperture());

    return apertures.fold(0d, (elem, acc) -> elem + acc) / apertures.size();
  }
}

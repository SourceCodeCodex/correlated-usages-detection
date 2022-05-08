package upt.ac.cti.core.pair.parameter.property;

import org.eclipse.jdt.core.ILocalVariable;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.dependency.Dependencies;

@PropertyComputer
public final class Coverage implements IPropertyComputer<Integer, MParameterPair> {

  @Override
  public Integer compute(MParameterPair mParameterPair) {
    @SuppressWarnings("unchecked")
    var pair = (Pair<ILocalVariable, ILocalVariable>) mParameterPair.getUnderlyingObject();

    var resolver = Dependencies.getParameterCoveredTypesResolver();

    var analysisResult = resolver.resolve(pair.getValue0(), pair.getValue1());

    if (analysisResult.isPresent()) {
      return analysisResult.get().size();
    }

    return -1;
  }

}

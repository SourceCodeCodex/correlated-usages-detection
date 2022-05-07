package upt.ac.cti.core.pair.parameter.property;

import static upt.ac.cti.dependencies.DependencyUtils.newParameterCoveredTypesResolver;
import org.eclipse.jdt.core.ILocalVariable;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class Coverage implements IPropertyComputer<Integer, MParameterPair> {

  @Override
  public Integer compute(MParameterPair mParameterPair) {
    @SuppressWarnings("unchecked")
    var pair = (Pair<ILocalVariable, ILocalVariable>) mParameterPair.getUnderlyingObject();

    var resolver = newParameterCoveredTypesResolver();

    var analysisResult = resolver.resolve(pair.getValue0(), pair.getValue1());

    if (analysisResult.isPresent()) {
      return analysisResult.get().size();
    }

    return -1;
  }

}

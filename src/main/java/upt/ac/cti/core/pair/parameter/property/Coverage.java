package upt.ac.cti.core.pair.parameter.property;

import org.eclipse.jdt.core.ILocalVariable;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import upt.ac.cti.coverage.ICoveredTypesResolver;

abstract class Coverage {

  protected abstract ICoveredTypesResolver<ILocalVariable> resolver();

  public Integer compute(MParameterPair mParameterPair) {
    @SuppressWarnings("unchecked")
    var pair = (Pair<ILocalVariable, ILocalVariable>) mParameterPair.getUnderlyingObject();

    var resolver = resolver();

    var analysisResult = resolver.resolve(pair.getValue0(), pair.getValue1());

    if (analysisResult.isPresent()) {
      return analysisResult.get().size();
    }

    return -1;
  }

}

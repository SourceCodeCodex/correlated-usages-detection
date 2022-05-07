package upt.ac.cti.core.pair.field.property;

import static upt.ac.cti.dependencies.DependencyUtils.newFieldCoveredTypesResolver;
import org.eclipse.jdt.core.IField;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class Coverage implements IPropertyComputer<Integer, MFieldPair> {

  @Override
  public Integer compute(MFieldPair mFieldPair) {
    @SuppressWarnings("unchecked")
    var pair = (Pair<IField, IField>) mFieldPair.getUnderlyingObject();

    var resolver = newFieldCoveredTypesResolver();

    var analysisResult = resolver.resolve(pair.getValue0(), pair.getValue1());

    if (analysisResult.isPresent()) {
      return analysisResult.get().size();
    }

    return -1;
  }

}

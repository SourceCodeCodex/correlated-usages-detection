package upt.ac.cti.core.pair.parameter.group;

import static upt.ac.cti.dependencies.DependencyUtils.newParameterCoveredTypesResolver;
import org.eclipse.jdt.core.ILocalVariable;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import familypolymorphismdetection.metamodel.entity.MTypePair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;

@RelationBuilder
public class CoveredTypePairs implements IRelationBuilder<MTypePair, MParameterPair> {


  @Override
  public Group<MTypePair> buildGroup(MParameterPair mParamterPair) {
    var group = new Group<MTypePair>();

    @SuppressWarnings("unchecked")
    var pair = (Pair<ILocalVariable, ILocalVariable>) mParamterPair.getUnderlyingObject();

    var resolver = newParameterCoveredTypesResolver();

    var factory = Factory.getInstance();

    var analysisResult = resolver.resolve(pair.getValue0(), pair.getValue1());

    if (analysisResult.isPresent()) {
      analysisResult.get().forEach(it -> group.add(factory.createMTypePair(it)));
    }

    return group;
  }
}

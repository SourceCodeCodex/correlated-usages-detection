package upt.ac.cti.core.pair.parameter.group;

import org.eclipse.jdt.core.ILocalVariable;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import familypolymorphismdetection.metamodel.entity.MTypePair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.coverage.ParameterCoveredTypesResolver;
import upt.ac.cti.coverage.parsing.CodeParser;
import upt.ac.cti.coverage.search.JavaEntitySearcher;

@RelationBuilder
public class CoveredTypePairs implements IRelationBuilder<MTypePair, MParameterPair> {


  @Override
  public Group<MTypePair> buildGroup(MParameterPair mParamterPair) {
    var group = new Group<MTypePair>();

    @SuppressWarnings("unchecked")
    var pair = (Pair<ILocalVariable, ILocalVariable>) mParamterPair.getUnderlyingObject();

    var resolver = new ParameterCoveredTypesResolver(new CodeParser(), new JavaEntitySearcher());

    var factory = Factory.getInstance();

    resolver.resolve(pair.getValue0(), pair.getValue1())
        .forEach(it -> group.add(factory.createMTypePair(it)));

    return group;
  }
}

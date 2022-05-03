package upt.ac.cti.core.pair.parameter.group;

import org.eclipse.jdt.core.ILocalVariable;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import familypolymorphismdetection.metamodel.entity.MTypePair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.aperture.ParameterAllTypePairsResolver;
import upt.ac.cti.util.binding.ParameterTypeBindingResolver;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;

@RelationBuilder
public final class AllTypePairs implements IRelationBuilder<MTypePair, MParameterPair> {

  @Override
  public Group<MTypePair> buildGroup(MParameterPair mParamterPair) {
    var resolver = new ParameterAllTypePairsResolver(new ParameterTypeBindingResolver(),
        new ConcreteDescendantsResolver());

    var group = new Group<MTypePair>();

    @SuppressWarnings("unchecked")
    var pair = (Pair<ILocalVariable, ILocalVariable>) mParamterPair.getUnderlyingObject();

    var typePairsStream = resolver.resolve(pair.getValue0(), pair.getValue1());

    var factory = Factory.getInstance();

    typePairsStream.forEach(it -> group.add(factory.createMTypePair(it)));

    return group;
  }


}

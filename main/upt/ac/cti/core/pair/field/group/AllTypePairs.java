package upt.ac.cti.core.pair.field.group;

import static upt.ac.cti.dependencies.DependencyUtils.newFieldAllTypePairsResolver;
import org.eclipse.jdt.core.IField;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MTypePair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;

@RelationBuilder
public final class AllTypePairs implements IRelationBuilder<MTypePair, MFieldPair> {

  @Override
  public Group<MTypePair> buildGroup(MFieldPair mFieldPair) {
    var resolver = newFieldAllTypePairsResolver();

    var group = new Group<MTypePair>();

    @SuppressWarnings("unchecked")
    var pair = (Pair<IField, IField>) mFieldPair.getUnderlyingObject();

    var typePairsStream = resolver.resolve(pair.getValue0(), pair.getValue1());

    var factory = Factory.getInstance();

    typePairsStream.forEach(it -> group.add(factory.createMTypePair(it)));

    return group;
  }

}

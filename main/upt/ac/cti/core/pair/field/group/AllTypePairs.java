package upt.ac.cti.core.pair.field.group;

import org.eclipse.jdt.core.IField;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MTypePair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.aperture.FieldAllTypePairsResolver;
import upt.ac.cti.util.binding.FieldTypeBindingResolver;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;

@RelationBuilder
public final class AllTypePairs implements IRelationBuilder<MTypePair, MFieldPair> {

  @Override
  public Group<MTypePair> buildGroup(MFieldPair mFieldPair) {
    var resolver = new FieldAllTypePairsResolver(new FieldTypeBindingResolver(), new ConcreteDescendantsResolver());

    var group = new Group<MTypePair>();

    @SuppressWarnings("unchecked")
    var pair = (Pair<IField, IField>) mFieldPair.getUnderlyingObject();

    var typePairsStream = resolver.resolve(pair.getValue0(), pair.getValue1());

    var factory = Factory.getInstance();

    typePairsStream.forEach(it -> group.add(factory.createMTypePair(it)));

    return group;
  }

}

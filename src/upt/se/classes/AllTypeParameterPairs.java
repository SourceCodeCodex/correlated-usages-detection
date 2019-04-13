package upt.se.classes;

import io.vavr.Tuple;
import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MTypePair;
import thesis.metamodel.factory.Factory;
import upt.se.utils.TypePair;
import upt.se.utils.builders.GroupBuilder;

@RelationBuilder
public class AllTypeParameterPairs implements IRelationBuilder<MTypePair, MClass> {

  @Override
  public Group<MTypePair> buildGroup(MClass entity) {
    return GroupBuilder.wrap(List.ofAll(entity.allTypeParameters().getElements())
        .sliding(2)
        .map(list2 -> Tuple.of(list2.get(0), list2.get(1)))
        .map(tuple -> Tuple.of(tuple._1.getUnderlyingObject(), tuple._2.getUnderlyingObject()))
        .map(tuple -> new TypePair(tuple._1, tuple._2))
        .map(Factory.getInstance()::createMTypePair)
        .toJavaList());
  }

}

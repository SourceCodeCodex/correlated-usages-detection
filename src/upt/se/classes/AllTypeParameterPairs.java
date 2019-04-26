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
    return Tuple.of(List.ofAll(entity.allTypeParameters().getElements()))
        .map(list -> Tuple.of(list, list))
        .map(tuple -> tuple._1.sliding(2).toList()
            .append(List.of(tuple._2.head(), tuple._2.reverse().head())))
        .map(list -> GroupBuilder.wrap(list
            .map(list2 -> Tuple.of(list2.get(0), list2.get(1)))
            .map(tuple -> Tuple.of(tuple._1.getUnderlyingObject(), tuple._2.getUnderlyingObject()))
            .map(tuple -> new TypePair(tuple._1, tuple._2))
            .map(Factory.getInstance()::createMTypePair)
            .toJavaList()))._1;
  }

}

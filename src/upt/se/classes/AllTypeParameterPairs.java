package upt.se.classes;

import io.vavr.Tuple;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MTypePair;
import thesis.metamodel.factory.Factory;
import upt.se.utils.TypePair;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class AllTypeParameterPairs implements IRelationBuilder<MTypePair, MClass> {

  @Override
  public Group<MTypePair> buildGroup(MClass entity) {
    return Tuple.of(GroupBuilder.unwrapArguments(entity.allTypeParameters()))
        .map(parameters -> Tuple.of(parameters, parameters))
        .map(parameters -> parameters._1()
            .sliding(2)
            .map(pair -> Tuple.of(pair.head(), pair.tail().head()))
            .toList().append(Tuple.of(parameters._2.head(), parameters._2.reverse().head())))
        .map(pairs -> GroupBuilder.wrap(pairs
            .map(pair -> new TypePair(pair._1, pair._2))
            .map(Factory.getInstance()::createMTypePair)))
        ._1();
  }

}

package upt.se.parameters.pair;

import io.vavr.Tuple;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MArgument;
import thesis.metamodel.entity.MParameterPair;
import thesis.metamodel.factory.Factory;
import upt.se.utils.ParameterPair;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class AllTypeParameterPairs implements IRelationBuilder<MParameterPair, MArgument> {

  @Override
  public Group<MParameterPair> buildGroup(MArgument entity) {
    return Tuple.of(GroupBuilder.unwrapParameters(entity.allTypeParameters()))
        .map(parameters -> Tuple.of(parameters, parameters))
        .map(parameters -> parameters._1()
            .sliding(2)
            .map(pair -> Tuple.of(pair.head(), pair.tail().head()))
            .toList().append(Tuple.of(parameters._2.head(), parameters._2.reverse().head())))
        .map(pairs -> GroupBuilder.wrap(pairs
            .map(pair -> new ParameterPair(pair._1, pair._2))
            .map(Factory.getInstance()::createMParameterPair)))
        ._1();
  }

}

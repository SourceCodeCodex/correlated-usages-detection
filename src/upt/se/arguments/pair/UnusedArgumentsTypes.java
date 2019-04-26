package upt.se.arguments.pair;

import static upt.se.utils.helpers.Equals.*;
import io.vavr.Tuple;
import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MTypePair;
import thesis.metamodel.factory.Factory;
import upt.se.utils.TypePair;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class UnusedArgumentsTypes implements IRelationBuilder<MTypePair, MTypePair> {

  @Override
  public Group<MTypePair> buildGroup(MTypePair entity) {
    return Tuple.of(entity.allArgumentsTypes(), entity.usedArgumentsTypes())
        .map(GroupBuilder::unwrapTypePairs, GroupBuilder::unwrapTypePairs)
        .apply((allTypes, usedTypes) -> allTypes.filter(type -> !contains(usedTypes, type)))
        .map(Factory.getInstance()::createMTypePair)
        .transform(GroupBuilder::wrap);
  }

  private boolean contains(List<TypePair> list, TypePair element) {
    return list.find(p -> isEqual(p.getFirst(), element.getFirst())
        && isEqual(p.getSecond(), element.getSecond())).isDefined();
  }

}

package upt.se.arguments.pair;

import static upt.se.utils.helpers.Equals.isEqual;
import io.vavr.Tuple;
import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClassPair;
import thesis.metamodel.entity.MParameterPair;
import thesis.metamodel.factory.Factory;
import upt.se.utils.ArgumentPair;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class UnusedArgumentsTypes implements IRelationBuilder<MClassPair, MParameterPair> {

  @Override
  public Group<MClassPair> buildGroup(MParameterPair entity) {
    return Tuple.of(entity.allPossibleArgumentsTypes(), entity.usedArgumentsTypes())
        .map(GroupBuilder::unwrapArgumentsPairs, GroupBuilder::unwrapArgumentsPairs)
        .apply((allTypes, usedTypes) -> allTypes.filter(type -> !contains(usedTypes, type)))
        .map(Factory.getInstance()::createMClassPair)
        .transform(GroupBuilder::wrap);
  }

  private boolean contains(List<ArgumentPair> usedTypes, ArgumentPair type) {
    return usedTypes.find(p -> isEqual(p.getFirst(), type.getFirst())
        && isEqual(p.getSecond(), type.getSecond())).isDefined();
  }

}

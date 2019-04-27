package upt.se.arguments.single;

import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MArgumentType;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class UsedArgumentTypes implements IRelationBuilder<MArgumentType, MArgumentType> {
  @Override
  public Group<MArgumentType> buildGroup(MArgumentType entity) {
    return GroupBuilder.wrap(List.empty());
  }

}

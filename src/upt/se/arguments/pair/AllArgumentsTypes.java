package upt.se.arguments.pair;

import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MTypePair;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class AllArgumentsTypes implements IRelationBuilder<MTypePair, MTypePair> {

  @Override
  public Group<MTypePair> buildGroup(MTypePair entity) {
    return GroupBuilder.wrap(List.empty());
  }

}

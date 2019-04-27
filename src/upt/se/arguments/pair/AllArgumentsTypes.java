package upt.se.arguments.pair;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MTypePair;
import upt.se.arguments.pair.internal.PairArgumentTypes;

@RelationBuilder
public class AllArgumentsTypes implements IRelationBuilder<MTypePair, MTypePair> {

  @Override
  public Group<MTypePair> buildGroup(MTypePair entity) {
    return PairArgumentTypes.allArgumentTypes(entity);
  }

}

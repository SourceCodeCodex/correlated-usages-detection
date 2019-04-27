package upt.se.arguments.single;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MArgumentType;
import upt.se.arguments.single.internal.SingleArgumentTypes;

@RelationBuilder
public class AllArgumentTypes implements IRelationBuilder<MArgumentType, MArgumentType> {

  @Override
  public Group<MArgumentType> buildGroup(MArgumentType entity) {
    return SingleArgumentTypes.allArgumentTypes(entity);
  }

}

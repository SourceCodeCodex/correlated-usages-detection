package upt.se.parameters;

import java.util.Collections;
import java.util.logging.Level;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MArgumentType;
import thesis.metamodel.factory.Factory;
import upt.se.utils.builders.GroupBuilder;
import upt.se.utils.store.ClassBindingStore;
import static upt.se.utils.helpers.ClassNames.*;
import static upt.se.utils.helpers.LoggerHelper.*;

@RelationBuilder
public class UsedArgumentTypes implements IRelationBuilder<MArgumentType, MArgumentType> {
  @Override
  public Group<MArgumentType> buildGroup(MArgumentType entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .filter(type -> !isObject(getFullName(type.getSuperclass())))
        .fold(object -> Try.success(ClassBindingStore.usagesInDeclaringClass(entity)),
              type -> Try.success(ClassBindingStore.usagesInInheritance(entity)))
        .map(List::ofAll)
        .map(list -> list.appendAll(ClassBindingStore.usagesInVariables(entity)))
        .map(list -> list.distinctBy((p1, p2) -> isEqual(p1, p2) ?  0 : 1))
        .map(types -> types.map(Factory.getInstance()::createMArgumentType))
        .map(List::toJavaList)
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(Collections.emptyList()))
        .map(GroupBuilder::wrap)
        .get();
  }

}

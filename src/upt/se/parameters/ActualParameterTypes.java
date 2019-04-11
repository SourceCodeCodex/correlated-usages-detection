package upt.se.parameters;

import static upt.se.utils.helpers.ClassNames.getFullName;
import static upt.se.utils.helpers.ClassNames.isObject;
import java.util.Collections;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.builders.GroupBuilder;
import upt.se.utils.store.ITypeBindingStore;

@RelationBuilder
public class ActualParameterTypes implements IRelationBuilder<MTypeParameter, MTypeParameter> {
  @Override
  public Group<MTypeParameter> buildGroup(MTypeParameter entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .filter(type -> isObject(getFullName(type.getSuperclass())))
        .fold(
            object -> Try.success(ITypeBindingStore.usagesInDeclaringClass(entity)),
            type -> Try.success(ITypeBindingStore.usagesInInheritance(entity)))
        .map(List::ofAll)
        .map(list -> list.appendAll(ITypeBindingStore.usagesInVariables(entity)))
        .map(types -> types.map(Factory.getInstance()::createMTypeParameter))
        .map(List::toJavaList)
        .map(GroupBuilder::create)
        .orElse(() -> Try.success(GroupBuilder.create(Collections.emptyList())))
        .get();
  }

}

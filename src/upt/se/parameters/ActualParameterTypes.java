package upt.se.parameters;

import static upt.se.utils.helpers.ClassNames.getFullName;
import static upt.se.utils.helpers.ClassNames.isObject;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.Collections;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.builders.GroupBuilder;
import upt.se.utils.store.ITypeStore;

@RelationBuilder
public class ActualParameterTypes implements IRelationBuilder<MTypeParameter, MTypeParameter> {
  @Override
  public Group<MTypeParameter> buildGroup(MTypeParameter entity) {
    Try.of(() -> entity.getUnderlyingObject())
        .filter(type -> isObject(getFullName(type.getSuperclass())))
        .fold(
            object -> Try.of(() -> Collections.<ITypeBinding>emptyList())
                .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred:" + t)),
            type -> Try.of(() -> Collections.<ITypeBinding>emptyList())
                .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred:" + t)))
        .map(List::ofAll)
        .map(types -> types.map(Factory.getInstance()::createMTypeParameter))
        .map(List::toJavaList)
        .map(GroupBuilder::create)
        .orElse(() -> Try.success(GroupBuilder.create(Collections.emptyList())))
        .get();

    Group<MClass> group = new Group<>();
    try {
      if (entity.getUnderlyingObject().getSuperclass().getQualifiedName()
          .equals(Object.class.getName())) {
        group.addAll(ITypeStore.declaringClassUsages(entity).stream()
            .map(Factory.getInstance()::createMClass).collect(Collectors.toList()));
      } else {
        group.addAll(ITypeStore.inheritanceUsages(entity).stream()
            .map(Factory.getInstance()::createMClass).collect(Collectors.toList()));
      }
      group.addAll(ITypeStore.attributesUsages(entity).stream()
          .map(Factory.getInstance()::createMClass).collect(Collectors.toList()));

    } catch (JavaModelException e) {
      e.printStackTrace();
    }

    Group<MClass> result = new Group<>();
    result.addAll(group.getElements().stream().distinct().collect(Collectors.toList()));
    return result;
  }

}

package upt.se.parameters;

import static io.vavr.API.For;
import static upt.se.utils.builders.ListBuilder.toList;
import static upt.se.utils.helpers.ClassNames.getFullName;
import static upt.se.utils.helpers.ClassNames.isObject;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.Collections;
import java.util.logging.Level;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MArgumentType;
import thesis.metamodel.factory.Factory;
import upt.se.utils.builders.GroupBuilder;
import upt.se.utils.builders.ListBuilder;
import upt.se.utils.store.ClassBindingStore;
import upt.se.utils.store.InterfaceBindingStore;

@RelationBuilder
public class AllArgumentTypes implements IRelationBuilder<MArgumentType, MArgumentType> {

  @Override
  public Group<MArgumentType> buildGroup(MArgumentType entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .flatMap(type -> For(classArgumentTypes(type), interfaceArgumentTypes(type))
                        .yield((classArguments, interfaceArguments) -> classArguments.appendAll(interfaceArguments)))
        .map(types -> types.map(Factory.getInstance()::createMArgumentType))
        .map(List::toJavaList)
        .orElse(() -> Try.success(Collections.emptyList()))
        .map(GroupBuilder::wrap)
        .get();
  }
  
  private Try<List<ITypeBinding>> classArgumentTypes(ITypeBinding entity) {
    return Try.of(() -> entity)
        .map(type -> type.getSuperclass())
        .filter(type -> !isObject(getFullName(type)))
        .fold(object -> Try.success(Collections.<ITypeBinding>emptyList()),
            type -> Try.of(() -> ListBuilder.toList(ClassBindingStore.getAllSubtypes(type))
                                            .prepend(entity.getSuperclass()))
                .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t)))
        .map(List::ofAll)
        .map(list -> list.appendAll(InterfaceBindingStore.getAllSubtypes(entity)))
        .orElse(() -> Try.success(toList(Collections.emptyList())));
  }
  
  private Try<List<ITypeBinding>> interfaceArgumentTypes(ITypeBinding entity) {
    return Try.of(() -> entity)
        .map(type -> toList(type.getInterfaces()))
        .filter(interfaces -> interfaces.size() == 0)
        .fold(empty -> Try.success(Collections.<ITypeBinding>emptyList()),
            interfaces -> Try.of(() -> interfaces.flatMap(type -> ClassBindingStore.getAllSubtypes(type))
                                            .prepend(entity.getSuperclass()))
                .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t)))
        .map(List::ofAll)
        .map(list -> list.appendAll(InterfaceBindingStore.getAllSubtypes(entity)))
        .orElse(() -> Try.success(toList(Collections.emptyList())));
  }

}

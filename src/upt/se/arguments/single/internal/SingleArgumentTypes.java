package upt.se.arguments.single.internal;

import static io.vavr.API.For;
import static upt.se.utils.helpers.Equals.isEqual;
import static upt.se.utils.helpers.Equals.isObject;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.logging.Level;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import thesis.metamodel.entity.MArgumentType;
import thesis.metamodel.factory.Factory;
import upt.se.utils.crawlers.InheritanceArgumentTypes;
import upt.se.utils.crawlers.VariablesArgumentTypes;
import upt.se.utils.helpers.GroupBuilder;

public class SingleArgumentTypes {
  
  public static Group<MArgumentType> allArgumentTypes(MArgumentType entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .flatMap(type -> For(classArgumentTypes(type), interfaceArgumentTypes(type))
            .yield((classArguments,
                interfaceArguments) -> isObject(classArguments.head())
                    && !interfaceArguments.isEmpty()
                        ? interfaceArguments
                        : classArguments.appendAll(interfaceArguments)))
        .map(types -> types.map(Factory.getInstance()::createMArgumentType))
        .orElse(() -> Try.success(List.empty()))
        .map(GroupBuilder::wrap)
        .get();
  }

  private static Try<List<ITypeBinding>> classArgumentTypes(ITypeBinding entity) {
    return Try.of(() -> entity)
        .map(type -> type.getSuperclass())
        .filter(type -> !isObject(type))
        .fold(object -> Try.success(List.of(entity.getSuperclass())),
            type -> Try.of(() -> InheritanceArgumentTypes.getAllSubtypes(type)
                .prepend(entity.getSuperclass())))
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t));
  }

  private static Try<List<ITypeBinding>> interfaceArgumentTypes(ITypeBinding entity) {
    return Try.of(() -> entity)
        .map(type -> List.of(type.getInterfaces()))
        .filter(interfaces -> !interfaces.isEmpty())
        .fold(empty -> Try.success(List.<ITypeBinding>empty()),
            interfaces -> Try
                .of(() -> interfaces.flatMap(type -> InheritanceArgumentTypes.getAllSubtypes(type))
                    .prependAll(List.of(entity.getInterfaces()))))
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t));
  }
  
  public static Group<MArgumentType> usedArgumentTypes(MArgumentType entity) {
    return Try.of(() -> InheritanceArgumentTypes.getUsages(entity))
        .map(List::ofAll)
        .map(usedTypes -> usedTypes.appendAll(VariablesArgumentTypes.getUsages(entity)))
        .map(list -> list.distinctBy((p1, p2) -> isEqual(p1, p2) ?  0 : 1))
        .map(types -> types.map(Factory.getInstance()::createMArgumentType))
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(List.empty()))
        .map(GroupBuilder::wrap)
        .get();
  }
}

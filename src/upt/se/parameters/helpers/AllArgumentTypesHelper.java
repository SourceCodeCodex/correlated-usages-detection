package upt.se.parameters.helpers;

import static upt.se.utils.builders.ListBuilder.toList;
import static upt.se.utils.helpers.ClassNames.getFullName;
import static upt.se.utils.helpers.ClassNames.isObject;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.Collections;
import java.util.logging.Level;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.collection.List;
import io.vavr.control.Try;
import upt.se.utils.builders.ListBuilder;
import upt.se.utils.store.ClassBindingStore;

public class AllArgumentTypesHelper {
  public static Try<List<ITypeBinding>> mockClassArgumentTypes(ITypeBinding entity) {
    return Try.of(() -> toList(Collections.emptyList()));
  }

  public static Try<List<ITypeBinding>> classArgumentTypes(ITypeBinding entity) {
    return Try.of(() -> entity)
        .map(type -> type.getSuperclass())
        .filter(type -> !isObject(getFullName(type)))
        .fold(object -> Try.success(Collections.singleton(entity.getSuperclass())),
            type -> Try.of(() -> ListBuilder.toList(ClassBindingStore.getAllSubtypes(type))
                .prepend(entity.getSuperclass()))
                .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t)))
        .map(List::ofAll)
        .orElse(() -> Try.success(toList(Collections.emptyList())));
  }

  public static Try<List<ITypeBinding>> interfaceArgumentTypes(ITypeBinding entity) {
    return Try.of(() -> entity)
        .map(type -> toList(type.getInterfaces()))
        .filter(interfaces -> !interfaces.isEmpty())
        .fold(empty -> Try.success(Collections.<ITypeBinding>emptyList()),
            interfaces -> Try
                .of(() -> interfaces.flatMap(type -> ClassBindingStore.getAllSubtypes(type))
                    .prependAll(toList(entity.getInterfaces())))
                .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t)))
        .map(List::ofAll)
        .orElse(() -> Try.success(toList(Collections.emptyList())));
  }
}

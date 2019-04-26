package upt.se.utils.crawlers.hierarchy;

import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import static upt.se.utils.store.ITypeStore.convert;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.Tuple;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class AllSubtypes {

  public static List<ITypeBinding> getAllSubtypes(ITypeBinding typeBinding) {
    return Try.of(() -> convert(typeBinding))
        .flatMap(maybeType -> Option.ofOptional(maybeType).toTry())
        .mapTry(type -> Tuple.of(type, type.newTypeHierarchy(new NullProgressMonitor())))
        .map(tuple -> tuple._2.getAllSubtypes(tuple._1))
        .map(types -> io.vavr.collection.List.of(types))
        .flatMap(list -> Try.of(() -> list.map(type -> convert(type))
            .map(Option::ofOptional)
            .map(Option::toTry)
            .map(Try::get)))
        .map(list -> list.toJavaList())
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(Collections.emptyList()))
        .get();
  }
}

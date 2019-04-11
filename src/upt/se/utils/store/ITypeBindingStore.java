package upt.se.utils.store;

import static upt.se.utils.store.ITypeStore.convert;
import java.util.Collections;
import java.util.List;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.Tuple;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class ITypeBindingStore {

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
        .orElse(() -> Try.success(Collections.emptyList()))
        .get();
  }
}

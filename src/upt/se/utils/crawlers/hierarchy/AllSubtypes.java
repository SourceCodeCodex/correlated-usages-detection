package upt.se.utils.crawlers.hierarchy;

import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import static upt.se.utils.store.ITypeStore.convert;
import java.util.logging.Level;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class AllSubtypes {

  public static List<ITypeBinding> getAllSubtypes(ITypeBinding typeBinding) {
    return Try.of(() -> convert(typeBinding))
        .flatMap(Option::toTry)
        .mapTry(type -> type.newTypeHierarchy(new NullProgressMonitor())
                            .getAllSubtypes(type))
        .map(List::of)
        .flatMap(subTypes -> convert(subTypes).toTry())
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(List.empty()))
        .get();
  }
}

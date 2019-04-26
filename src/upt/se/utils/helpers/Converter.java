package upt.se.utils.helpers;

import static java.util.function.Function.identity;
import static upt.se.utils.helpers.Equals.isEqual;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import upt.se.utils.visitors.GenericParameterBindingVisitor;
import upt.se.utils.visitors.HierarchyBindingVisitor;

public final class Converter {

  private static Lazy<Map<String, IType>> allTypes =
      Lazy.of(() -> List.of(ResourcesPlugin.getWorkspace().getRoot().getProjects())
          .flatMap(project -> Try.of(() -> JavaCore.create(project))
              .mapTry(javaProject -> javaProject.getPackageFragments())
              .map(packages -> List.of(packages))
              .map(packages -> packages
                  .flatMap(singlePackage -> Try
                      .of(() -> List.of(singlePackage.getOrdinaryClassFiles())
                          .map(classFile -> classFile.getType())
                          .map(type -> Tuple.of(type.getFullyQualifiedName(), type)))
                      .orElse(() -> Try.success(List.empty()))
                      .get()))
              .orElse(() -> Try.success(List.empty()))
              .get())
          .toMap(identity()));


  public static Map<String, IType> getAllTypes() {
    return allTypes.get();
  }

  public static final Option<IType> convert(ITypeBinding typeBinding) {
    return getAllTypes().get(typeBinding.getQualifiedName());
  }

  public static final Option<ITypeBinding> convert(IType type) {
    return List.ofAll(GenericParameterBindingVisitor.convert(type.getCompilationUnit()))
        .find(typeBinding -> isEqual(type, typeBinding))
        .orElse(List.ofAll(HierarchyBindingVisitor.convert(type.getCompilationUnit()))
            .find(typeBinding -> isEqual(type, typeBinding)));
  }

  public static final Option<List<ITypeBinding>> convert(List<IType> types) {
    return types.foldLeft(Option.some(List.empty()),
        (res, type) -> convert(type).map(typeBinding -> res.get().append(typeBinding)));
  }

}

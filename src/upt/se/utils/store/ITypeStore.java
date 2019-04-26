package upt.se.utils.store;

import static upt.se.utils.helpers.ClassNames.*;
import static java.util.function.Function.identity;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
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

public final class ITypeStore {
  private static Lazy<List<IType>> allTypes =
      Lazy.of(() -> List.of(ResourcesPlugin.getWorkspace().getRoot().getProjects())
          .flatMap(project -> Try.of(() -> JavaCore.create(project))
              .mapTry(javaProject -> javaProject.getPackageFragments())
              .map(packages -> List.of(packages))
              .map(packages -> packages
                  .flatMap(singlePackage -> Try
                      .of(() -> List.of(singlePackage.getOrdinaryClassFiles())
                          .map(classFile -> classFile.getType()))
                      .orElse(() -> Try.success(List.empty()))
                      .get()))
              .orElse(() -> Try.success(List.empty()))
              .get()));

  {
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
  }


  public static final List<IType> getAllTypes(IJavaProject javaproject) {

    List<IType> typeList = List.empty();
    try {
      IPackageFragmentRoot[] roots = javaproject.getPackageFragmentRoots();
      for (int i = 0; i < roots.length; i++) {
        IPackageFragmentRoot root = roots[i];
        IJavaElement[] javaElements = root.getChildren();
        for (int j = 0; j < javaElements.length; j++) {
          IJavaElement javaElement = javaElements[j];
          if (javaElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
            IPackageFragment pf = (IPackageFragment) javaElement;
            ICompilationUnit[] compilationUnits = pf.getCompilationUnits();
            for (int k = 0; k < compilationUnits.length; k++) {
              ICompilationUnit unit = compilationUnits[k];
              if (unit.isStructureKnown()) {
                typeList.appendAll(List.of(unit.getTypes()));
              }
            }
          }
        }
      }
    } catch (CoreException e) {
      e.printStackTrace();
    }

    return typeList;
  }

  public static List<IType> getAllTypes() {
    return allTypes.get();
  }

  public static final Option<IType> convert(ITypeBinding typeBinding) {
    return getAllTypes().find(type -> isEqual(type, typeBinding));
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

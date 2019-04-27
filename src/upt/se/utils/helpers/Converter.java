package upt.se.utils.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOrdinaryClassFile;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import upt.se.utils.visitors.ITypeConverter;

public final class Converter {

  private static Lazy<Map<String, IType>> allTypes =
      Lazy.of(() -> {
        Map<String, IType> classList = HashMap.empty();

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        IProject[] projects = root.getProjects();

        for (IProject project : projects) {
          IJavaProject javaProject = JavaCore.create(project);
          try {
            IPackageFragment[] packages = javaProject.getPackageFragments();

            for (IPackageFragment myPackage : packages) {
              try {
                IOrdinaryClassFile[] classes = myPackage.getOrdinaryClassFiles();

                for (IOrdinaryClassFile myClass : classes) {
                  classList = classList
                      .put(Tuple.of(myClass.getType().getFullyQualifiedName(), myClass.getType()));
                }

                ICompilationUnit[] compilationUnits = myPackage.getCompilationUnits();

                for (ICompilationUnit compilationUnit : compilationUnits) {
                  IType[] types = compilationUnit.getTypes();
                  for (IType type : types) {
                    classList = classList.put(Tuple.of(type.getFullyQualifiedName(), type));
                  }
                }
              } catch (JavaModelException ex) {
                System.out.println("getOrdinaryClassFiles");
              }
            }
          } catch (JavaModelException ex) {
            System.out.println("getPackageFragments");
          }
        }

        return classList;
      });

  public static Map<String, IType> getAllTypes() {
    return allTypes.get();
  }

  public static final Option<IType> convert(ITypeBinding typeBinding) {
    return getAllTypes().get(typeBinding.getQualifiedName());
  }

  public static final Option<ITypeBinding> convert(IType type) {
    return ITypeConverter.convert(type);
  }

  public static final Option<List<ITypeBinding>> convert(List<IType> types) {
    System.out.println(types.filter(type -> type.getCompilationUnit() == null).mkString("\n"));
    return types.foldLeft(Option.some(List.empty()),
        (res, type) -> convert(type).map(typeBinding -> res.get().append(typeBinding)));
  }

  public static double round(double value, int places) {
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}

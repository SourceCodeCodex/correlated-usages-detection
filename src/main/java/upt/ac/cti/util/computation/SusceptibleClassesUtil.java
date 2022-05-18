package upt.ac.cti.util.computation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MProject;
import familypolymorphismdetection.metamodel.factory.Factory;
import upt.ac.cti.dependency.Dependencies;

public class SusceptibleClassesUtil {

  public static List<IType> allTypes(MProject mProject) {
    var allTypes = new ArrayList<IType>();

    var javaProject = (IJavaProject) mProject.getUnderlyingObject();

    try {
      for (IPackageFragmentRoot packageFragmentRoot : javaProject.getAllPackageFragmentRoots()) {
        for (IJavaElement javaElement : packageFragmentRoot.getChildren()) {
          if (javaElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
            var packageFragment = (IPackageFragment) javaElement;
            for (ICompilationUnit cu : packageFragment.getCompilationUnits()) {
              if (cu.isStructureKnown()) {
                allTypes.addAll(List.of(cu.getAllTypes()));
              }
            }
          }
        }
      }
    } catch (JavaModelException e) {
      e.printStackTrace();
    }

    return allTypes.stream().distinct().toList();
  }


  public static Stream<MClass> filterClasses(Stream<IType> allTypes) {
    var typeValidator = Dependencies.typeValidator;

    return allTypes.parallel()
        .filter(typeValidator)
        .map(type -> Factory.getInstance().createMClass(type));
  }

}

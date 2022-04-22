package upt.ac.cti.model.project.group;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;

@RelationBuilder
public final class UnparametrizedClasses implements IRelationBuilder<MClass, MProject> {

  @Override
  public Group<MClass> buildGroup(MProject mProject) {
    var result = new Group<MClass>();
    var javaProject = (IJavaProject) mProject.getUnderlyingObject();
    try {
      for (IPackageFragmentRoot packageFragmentRoot : javaProject.getAllPackageFragmentRoots()) {
        for (IJavaElement javaElement : packageFragmentRoot.getChildren()) {
          if (javaElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
            var packageFragment = (IPackageFragment) javaElement;
            for (ICompilationUnit cu : packageFragment.getCompilationUnits()) {
              if (cu.isStructureKnown()) {
                for (IType aType : cu.getAllTypes()) {
                  var mClass = Factory.getInstance().createMClass(aType);
                  if (aType.getTypeParameters().length == 0
                      && mClass.fieldPairs().getElements().size() != 0) {
                    result.add(mClass);
                  }
                }
              }
            }
          }
        }
      }
    } catch (JavaModelException e) {
      e.printStackTrace();
    }
    return result;
  }

}

package upt.ac.cti.core.project.group;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
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
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.util.logging.RLogger;
import upt.ac.cti.util.time.StopWatch;
import upt.ac.cti.util.validation.SusceptibleTypeValidator;

@RelationBuilder
public final class FamilyPolymorphismSusceptibleClasses
    implements IRelationBuilder<MClass, MProject> {

  private static final Logger logger = RLogger.get();

  @Override
  public Group<MClass> buildGroup(MProject mProject) {
    var group = new Group<MClass>();

    var javaProject = (IJavaProject) mProject.getUnderlyingObject();

    var stopWatch = new StopWatch();
    logger
        .info("Start searching susceptible classes for project: " + javaProject.getElementName());
    stopWatch.start();

    var allTypes = new ArrayList<IType>();

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
      logger.severe("Susceptible classes not resolved due to: " + e.getMessage());
      e.printStackTrace();
    }

    var typeValidator = new SusceptibleTypeValidator();
    var mClasses = allTypes.parallelStream()
        .filter(typeValidator)
        .map(type -> Factory.getInstance().createMClass(type))
        .toList();

    group.addAll(mClasses);

    stopWatch.end();


    logger
        .info("Susceptible classes: " + mClasses.size() + " out of " + allTypes.size());
    logger
        .info("Time to resolve: " + stopWatch.getDuration().toMillis() + "ms");

    return group;
  }

}

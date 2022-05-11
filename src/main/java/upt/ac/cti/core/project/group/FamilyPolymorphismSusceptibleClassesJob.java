package upt.ac.cti.core.project.group;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
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
import upt.ac.cti.util.logging.RLogger;
import upt.ac.cti.util.time.StopWatch;

public class FamilyPolymorphismSusceptibleClassesJob extends Job {

  private static final Logger logger = RLogger.get();

  private final MProject mProject;

  private List<MClass> mClasses = List.of();

  public List<MClass> mClasses() {
    return mClasses;
  }

  public FamilyPolymorphismSusceptibleClassesJob(MProject mProject) {
    super("Susceptible " + mProject + " family polymorphism classes");
    this.mProject = mProject;

  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {

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

    logger.info("Found classes in project: " + allTypes.size());
    var subMonitor = SubMonitor.convert(monitor, allTypes.size());

    var typeValidator = Dependencies.getSusceptibleTypeValidator();

    var mClasses =
        allTypes.parallelStream()
            .peek(m -> subMonitor.split(1))
            .filter(typeValidator)
            .map(type -> Factory.getInstance().createMClass(type))
            .toList();

    this.mClasses = mClasses;

    logger.info("Searching susceptible classes progress: 100%");


    stopWatch.stop();

    logger
        .info("Susceptible classes: " + mClasses.size() + " out of " + allTypes.size());
    logger
        .info("Time to resolve: " + stopWatch.getDuration().toMillis() + "ms");

    return Status.OK_STATUS;

  }

}

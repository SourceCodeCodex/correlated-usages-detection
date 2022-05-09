package upt.ac.cti.core.project.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.logging.RLogger;
import upt.ac.cti.util.time.StopWatch;

@RelationBuilder
public final class FamilyPolymorphismSusceptibleClasses
    implements IRelationBuilder<MClass, MProject> {

  private static final int CHUNK_SIZE = 128;

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

    logger.info("Found classes in project: " + allTypes.size());
    var typeValidator = Dependencies.getSusceptibleTypeValidator();
    var chunks = allTypes.size() / CHUNK_SIZE + ((allTypes.size() % CHUNK_SIZE == 0) ? 0 : 1);
    var processed = new AtomicInteger(0);

    var mClasses = chunked(allTypes.stream(), CHUNK_SIZE)
        .parallel()
        .map(l -> l.parallelStream()
            .filter(typeValidator)
            .map(type -> Factory.getInstance().createMClass(type)))
        .peek(l -> {
          synchronized (processed) {
            logger.info("Searching susceptible classes progress: "
                + 100 * (processed.getAndIncrement()) / chunks + "%");
          }
        })
        .flatMap(l -> l)
        .toList();

    logger.info("Searching susceptible classes progress: 100%");

    group.addAll(mClasses);

    stopWatch.stop();

    logger
        .info("Susceptible classes: " + mClasses.size() + " out of " + allTypes.size());
    logger
        .info("Time to resolve: " + stopWatch.getDuration().toMillis() + "ms");

    return group;
  }

  private static <T> Stream<List<T>> chunked(Stream<T> stream, int chunkSize) {
    var index = new AtomicInteger(0);

    return stream.collect(Collectors.groupingBy(x -> index.getAndIncrement() / chunkSize))
        .entrySet().stream()
        .sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue);
  }

}

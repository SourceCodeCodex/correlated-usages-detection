package upt.ac.cti.core.workingset.group;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MWorkingSet;
import upt.ac.cti.util.computation.SusceptibleClassesUtil;
import upt.ac.cti.util.logging.RLogger;
import upt.ac.cti.util.time.StopWatch;

public class WSFamilyPolymorphismSusceptibleClassesJob extends Job {

  private static final Logger logger = RLogger.get();

  private final MWorkingSet mWorkingSet;

  private final List<MClass> mClasses = new ArrayList<>();

  public List<MClass> mClasses() {
    return mClasses;
  }

  public WSFamilyPolymorphismSusceptibleClassesJob(MWorkingSet mWorkingSet) {
    super("Susceptible " + mWorkingSet + " (working set) family polymorphism classes");
    this.mWorkingSet = mWorkingSet;

  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {

    var stopWatch = new StopWatch();
    logger
        .info("Start searching susceptible classes for workig set: " + mWorkingSet);
    stopWatch.start();

    var allTypes = mWorkingSet.javaProjects().getElements().stream()
        .flatMap(p -> SusceptibleClassesUtil.allTypes(p).stream())
        .toList();

    var subMonitor = SubMonitor.convert(monitor, allTypes.size());

    logger.info("Found classes in working set: " + allTypes.size());

    var mClasses =
        SusceptibleClassesUtil
            .filterClasses(allTypes.parallelStream().peek(m -> subMonitor.split(1))).toList();

    this.mClasses.addAll(mClasses);

    stopWatch.stop();

    logger
        .info("Susceptible classes: " + mClasses.size() + " out of " + allTypes.size());
    logger
        .info("Time to resolve: " + stopWatch.getDuration().toMillis() + "ms");

    return Status.OK_STATUS;

  }

}

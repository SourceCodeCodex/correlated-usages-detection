package upt.ac.cti.core.project.group;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MProject;
import upt.ac.cti.config.Config;
import upt.ac.cti.util.computation.SusceptibleClassesUtil;
import upt.ac.cti.util.logging.RLogger;
import upt.ac.cti.util.time.StopWatch;

public class FamilyPolymorphismSusceptibleClassesJob extends Job {

  private static final Logger logger = RLogger.get();

  private final MProject mProject;

  private final String jobFamily = Config.JOB_FAMILY;

  private final List<MClass> mClasses = new ArrayList<>();

  public List<MClass> mClasses() {
    return mClasses;
  }

  public FamilyPolymorphismSusceptibleClassesJob(MProject mProject) {
    super("Susceptible " + mProject + " family polymorphism classes");
    this.mProject = mProject;

  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {

    var stopWatch = new StopWatch();
    logger
        .info("Start searching susceptible classes for project: " + mProject);
    stopWatch.start();

    var allTypes = SusceptibleClassesUtil.allTypes(mProject);

    logger.info("Found classes in project: " + allTypes.size());

    var subMonitor = SubMonitor.convert(monitor, allTypes.size());

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

  @Override
  public boolean belongsTo(Object family) {
    return this.jobFamily.equals(family);
  }

}

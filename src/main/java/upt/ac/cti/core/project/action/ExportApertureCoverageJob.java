package upt.ac.cti.core.project.action;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MProject;
import upt.ac.cti.config.Config;
import upt.ac.cti.core.project.group.FamilyPolymorphismSusceptibleClassesJob;
import upt.ac.cti.coverage.CoverageStrategy;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.logging.RLogger;
import upt.ac.cti.util.report.ReportUtil;

class ExportApertureCoverageJob extends Job {

  private final Logger logger;

  private static ForkJoinPool pool = new ForkJoinPool(Config.CLASS_ANALYSIS_PARALLELISM);

  private final MProject mProject;

  private final String jobFamily = Config.JOB_FAMILY;

  private final Set<CoverageStrategy> strategies;

  public ExportApertureCoverageJob(
      MProject mProject,
      Set<CoverageStrategy> strategies) {
    super("Export " + mProject + " aperture coverage report - "
        + String.join("-", strategies.stream().map(s -> s.name).toList()));

    logger = RLogger.get(this.getClass().getSimpleName() + "-"
        + String.join("-", strategies.stream().map(s -> s.name).toList()));

    this.mProject = mProject;
    this.strategies = strategies;
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    Dependencies.init();

    var mClassesJob = new FamilyPolymorphismSusceptibleClassesJob(mProject);
    mClassesJob.setPriority(Job.LONG);
    mClassesJob.setSystem(false);
    mClassesJob.setUser(true);
    mClassesJob.schedule();


    try {
      mClassesJob.join();
      var mClasses = mClassesJob.mClasses();
      var subMonitor = SubMonitor.convert(monitor, mClasses.size());

      var stream = pool.submit(() -> mClasses.parallelStream()
          .map(mClass -> Pair.with(mClass.toString(), strategies.stream().map(s -> {
            try {
              return s.apertureCoverage.apply(mClass);
            } catch (Exception e) {
              e.printStackTrace();
              return -2.;
            }
          }))).peek(l -> subMonitor.split(1))).get();
      ReportUtil.createReport(mProject.toString(), strategies, stream);
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }

    logger.info("Export progress: 100%");
    logger.info("Report exported succesfully");

    return Status.OK_STATUS;

  }

  @Override
  public boolean belongsTo(Object family) {
    return this.jobFamily.equals(family);
  }

}

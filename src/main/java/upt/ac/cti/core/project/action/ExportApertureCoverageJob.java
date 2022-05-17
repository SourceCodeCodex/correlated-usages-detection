package upt.ac.cti.core.project.action;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.logging.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MClass;
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

  private final CoverageStrategy coverageStrategy;

  private final Function<MClass, Double> apertureCoverage;

  public ExportApertureCoverageJob(MProject mProject, Function<MClass, Double> apertureCoverage,
      CoverageStrategy strategy) {
    super("Export " + mProject + " aperture coverage report - " + strategy.name);
    this.mProject = mProject;
    this.apertureCoverage = apertureCoverage;
    this.coverageStrategy = strategy;
    logger = RLogger.get(this.getClass().getSimpleName() + "-" + strategy.name);
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

      var stream = pool.submit(() -> mClasses.stream().map(mClass -> {
        try {
          return Pair.with(mClass.toString(), apertureCoverage.apply(mClass));
        } catch (Exception e) {
          e.printStackTrace();
          return Pair.with(mClass.toString(), -2.);
        }
      }).peek(l -> subMonitor.split(1))).get();
      ReportUtil.createReport(mProject.toString(), coverageStrategy, stream);
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

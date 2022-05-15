package upt.ac.cti.core.workingset.action;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MWorkingSet;
import upt.ac.cti.config.Config;
import upt.ac.cti.core.workingset.group.WSFamilyPolymorphismSusceptibleClassesJob;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.logging.RLogger;
import upt.ac.cti.util.report.ReportUtil;

public class ExportApertureCoverageJob extends Job {

  private final Logger logger = RLogger.get();

  private static ForkJoinPool pool = new ForkJoinPool(Config.CLASS_ANALYSIS_PARALLELISM);

  private final MWorkingSet mWorkingSet;

  public ExportApertureCoverageJob(MWorkingSet mWorkingSet) {
    super("Export " + mWorkingSet + " (working set) aperture coverage report");
    this.mWorkingSet = mWorkingSet;
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    Dependencies.init();

    var mClassesJob = new WSFamilyPolymorphismSusceptibleClassesJob(mWorkingSet);
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
          return Pair.with(mClass.toString(), mClass.apertureCoverage());
        } catch (Exception e) {
          e.printStackTrace();
          return Pair.with(mClass.toString(), -2.);
        }
      }).peek(l -> subMonitor.split(1))).get();
      ReportUtil.createReport(mWorkingSet.toString(), stream);
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }

    logger.info("Export progress: 100%");
    logger.info("Report exported succesfully");

    return Status.OK_STATUS;

  }

}

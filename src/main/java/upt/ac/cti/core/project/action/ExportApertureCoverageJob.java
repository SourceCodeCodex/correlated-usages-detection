package upt.ac.cti.core.project.action;

import java.util.List;
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
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.logging.RLogger;
import upt.ac.cti.util.report.ReportUtil;

public class ExportApertureCoverageJob extends Job {

  private final Logger logger = RLogger.get();

  private final MProject mProject;

  public ExportApertureCoverageJob(MProject mProject) {
    super("Export " + mProject + " aperture coverage report");
    this.mProject = mProject;
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {

    var config = new Config();
    Dependencies.init(config);


    var mClassesJob = new FamilyPolymorphismSusceptibleClassesJob(mProject);
    mClassesJob.setPriority(Job.LONG);
    mClassesJob.setSystem(false);
    mClassesJob.setUser(true);
    mClassesJob.schedule();

    List<MClass> mClasses;
    try {
      mClassesJob.join();
      mClasses = mClassesJob.mClasses();
    } catch (InterruptedException e1) {
      e1.printStackTrace();
      mClasses = List.of();
    }

    var subMonitor = SubMonitor.convert(monitor, mClasses.size());

    var stream =
        mClasses.parallelStream()
            .map(mClass -> {
              try {
                return Pair.with(mClass.toString(), mClass.apertureCoverage());
              } catch (Exception e) {
                e.printStackTrace();
                return Pair.with(mClass.toString(), -2.);
              }
            })
            .peek(l -> subMonitor.split(1));

    ReportUtil.createReport(mProject.toString(), stream, config);
    logger.info("Export progress: 100%");
    logger.info("Report exported succesfully");

    return Status.OK_STATUS;

  }

}

package upt.ac.cti.core.project.action;

import org.eclipse.core.runtime.jobs.Job;
import familypolymorphismdetection.metamodel.entity.MProject;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;
import upt.ac.cti.util.report.ReportUtil;

@ActionPerformer
public class ExportReport implements IActionPerformer<Void, MProject, HListEmpty> {

  /**
   * Used for testing only! Please do not change this value!
   */
  public static volatile boolean BLOCKING = false;

  @Override
  public Void performAction(MProject mProject, HListEmpty arg1) {

    var job = new ExportApertureCoverageJob(mProject);

    job.setPriority(Job.LONG);
    job.setRule(ReportUtil.MUTEX_RULE);
    job.setSystem(false);
    job.setUser(true);
    job.schedule();

    if (BLOCKING) {
      try {
        job.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

}

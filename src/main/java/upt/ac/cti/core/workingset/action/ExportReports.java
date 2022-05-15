package upt.ac.cti.core.workingset.action;

import org.eclipse.core.runtime.jobs.Job;
import familypolymorphismdetection.metamodel.entity.MWorkingSet;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;
import upt.ac.cti.util.report.ReportUtil;

@ActionPerformer
public class ExportReports implements IActionPerformer<Void, MWorkingSet, HListEmpty> {

  @Override
  public Void performAction(MWorkingSet mWorkingSet, HListEmpty arg1) {
    var job = new ExportApertureCoverageJob(mWorkingSet);

    job.setPriority(Job.LONG);
    job.setRule(ReportUtil.MUTEX_RULE);
    job.setSystem(false);
    job.setUser(true);
    job.schedule();

    return null;
  }

}

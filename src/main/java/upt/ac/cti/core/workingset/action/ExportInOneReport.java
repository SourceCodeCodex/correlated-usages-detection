package upt.ac.cti.core.workingset.action;

import java.util.function.Function;
import org.eclipse.core.runtime.jobs.Job;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MWorkingSet;
import ro.lrg.xcore.metametamodel.HListEmpty;
import upt.ac.cti.coverage.CoverageStrategy;
import upt.ac.cti.util.report.ReportUtil;

abstract class ExportInOneReport {
  protected abstract Function<MClass, Double> apertureCoverage();

  protected abstract CoverageStrategy strategyName();

  public Void performAction(MWorkingSet mWorkingSet, HListEmpty arg1) {
    var job = new ExportInOneJob(mWorkingSet, apertureCoverage(), strategyName());

    job.setPriority(Job.LONG);
    job.setRule(ReportUtil.MUTEX_RULE);
    job.setSystem(false);
    job.setUser(true);
    job.schedule();

    return null;
  }

}

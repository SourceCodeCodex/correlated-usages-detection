package upt.ac.cti.core.workingset.action;

import java.util.function.Function;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MWorkingSet;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;
import upt.ac.cti.coverage.CoverageStrategy;

@ActionPerformer
public class ExportInOneReportNameSimilarity extends ExportInOneReport
    implements IActionPerformer<Void, MWorkingSet, HListEmpty> {

  @Override
  protected Function<MClass, Double> apertureCoverage() {
    return MClass::apertureCoverageNameSimilarity;
  }

  @Override
  protected CoverageStrategy strategyName() {
    return CoverageStrategy.NAME_SIMILARITY;
  }

}

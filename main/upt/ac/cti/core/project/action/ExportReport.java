package upt.ac.cti.core.project.action;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MProject;
import ro.lrg.xcore.metametamodel.ActionPerformer;
import ro.lrg.xcore.metametamodel.HListEmpty;
import ro.lrg.xcore.metametamodel.IActionPerformer;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.Chunker;
import upt.ac.cti.util.logging.RLogger;

@ActionPerformer
public class ExportReport implements IActionPerformer<Void, MProject, HListEmpty> {

  private final int CHUNK_SIZE = 16;

  private final Logger logger = RLogger.get();

  @Override
  public Void performAction(MProject mProject, HListEmpty arg1) {
    logger.info("Reset cache");
    Dependencies.init();

    var mClasses = mProject.familyPolymorphismSusceptibleClasses();

    var allClasses = mClasses.getElements().size();

    var chunks = allClasses / CHUNK_SIZE + ((allClasses % CHUNK_SIZE == 0) ? 0 : 1);
    var processed = new AtomicInteger(0);

    var stream = Chunker.chunked(mClasses.getElements().stream(), CHUNK_SIZE)
        .map(l -> l.parallelStream()
            .map(mClass -> Pair.with(mClass.toString(), mClass.apertureCoverage())))
        .peek(l -> {
          logger.info("Export progress: " + 100 * (processed.getAndIncrement()) / chunks + "%");
        })
        .flatMap(s -> s);

    ReportUtil.createReport(mProject.toString(), stream);
    logger.info("Export progress: 100%");
    logger.info("Report exported succesfully: ");
    return null;
  }

}

package upt.ac.cti.analysis.coverage.flow.insensitive.deriver;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.util.FieldWritingBindingResolver;

public class FieldWritingsDeriver implements IFieldWritingsDeriver {

  private final LinkedBlockingQueue<Pair<FieldWriting, FieldWriting>> writingPairs =
      new LinkedBlockingQueue<>();
  private final Set<Pair<FieldWriting, FieldWriting>> derived =
      Collections.synchronizedSet(new HashSet<>());
  private final Set<Pair<ITypeBinding, ITypeBinding>> typePairs =
      Collections.synchronizedSet(new HashSet<>());;

  private static final int POOL_SIZE = 4;
  private final ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);

  private final FieldWritingBindingResolver assignmentBindingResolver;

  private static final Logger logger = Logger.getLogger(FieldWritingsDeriver.class.getSimpleName());

  public FieldWritingsDeriver(FieldWritingBindingResolver assignmentBindingResolver) {
    this.assignmentBindingResolver = assignmentBindingResolver;
  }

  @Override
  public Set<Pair<ITypeBinding, ITypeBinding>> derive(
      List<Pair<FieldWriting, FieldWriting>> input) {

    var counter = 0;
    List<Future<DerivationResult>> futures;

    while (!writingPairs.isEmpty()) {
      writingPairs.removeAll(derived);
      logger.info(String.format("Start set %d of derivations", counter++));

      try {
        futures = pool.invokeAll(writingPairs.parallelStream()
            .map(p -> new DerivationCallable(assignmentBindingResolver, p))
            .toList());
      } catch (InterruptedException e) {
        var ste = e.getStackTrace()[0];
        logger.throwing(ste.getClassName(), ste.getMethodName(), e);

        return typePairs;
      }

      derived.addAll(writingPairs);
      writingPairs.clear();

      for (Future<DerivationResult> f : futures) {
        DerivationResult result;
        try {
          result = f.get();
        } catch (InterruptedException | ExecutionException e) {
          var ste = e.getStackTrace()[0];
          logger.throwing(ste.getClassName(), ste.getMethodName(), e);

          return typePairs;
        }
        writingPairs.addAll(result.newPairs());

        if (result.resolvedCorelation().isPresent()) {
          var corelation = result.resolvedCorelation().get();
          logger.info("Resolved type pair: " + corelation.getValue0().getQualifiedName() + " & "
              + corelation.getValue1().getQualifiedName());

          typePairs.add(corelation);
        }
      }
    }
    return typePairs;
  }
}

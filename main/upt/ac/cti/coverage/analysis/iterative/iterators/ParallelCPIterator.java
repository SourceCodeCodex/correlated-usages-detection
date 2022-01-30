package upt.ac.cti.coverage.analysis.iterative.iterators;

import java.util.HashSet;
import java.util.LinkedList;
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
import upt.ac.cti.coverage.analysis.iterative.generators.CPGenerator;
import upt.ac.cti.coverage.analysis.iterative.model.CPHandlingResult;
import upt.ac.cti.coverage.analysis.iterative.model.CorelationPair;

public class ParallelCPIterator {
  private static final int POOL_SIZE = 1;
  private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

  private final LinkedBlockingQueue<CorelationPair> pairs = new LinkedBlockingQueue<>();
  private final Set<CorelationPair> handled = new HashSet<>();
  private final ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
  private final List<Pair<ITypeBinding, ITypeBinding>> corelations = new LinkedList<>();

  public ParallelCPIterator(CPGenerator generator) {
    pairs.addAll(generator.generate());
  }

  public List<Pair<ITypeBinding, ITypeBinding>> corelations()
      throws InterruptedException, ExecutionException {
    List<Future<CPHandlingResult>> futures;
    while (!pairs.isEmpty()) {
      pairs.removeAll(handled);
      logger.info("Invoke new set of tasks!");
      futures = pool.invokeAll(pairs.parallelStream().map(CPHandlingCallable::new).toList());
      handled.addAll(pairs);
      pairs.clear();
      for (Future<CPHandlingResult> f : futures) {
        var result = f.get();
        pairs.addAll(result.newPairs());
        if (result.resolvedCorelation().isPresent()) {
          var corelation = result.resolvedCorelation().get();
          logger.info("Resolved corelation: " + corelation.getValue0().getQualifiedName() + " & "
              + corelation.getValue1().getQualifiedName());
          corelations.add(corelation);
        }
      }

    }
    return corelations;
  }
}

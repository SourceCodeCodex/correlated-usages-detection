package upt.ac.cti.analysis.coverage.flow.insensitive.derivator;

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
import upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.FieldWritingsDerivator;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.NewWritingPairs;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.ResolvedBindings;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.JavaEntitySearcher;
import upt.ac.cti.analysis.coverage.flow.insensitive.util.FieldWritingBindingResolver;

public class DerivationManager implements IDerivationManager {

  private final LinkedBlockingQueue<Pair<FieldWriting, FieldWriting>> writingPairs =
      new LinkedBlockingQueue<>();
  private final Set<Pair<FieldWriting, FieldWriting>> derived =
      Collections.synchronizedSet(new HashSet<>());
  private final Set<Pair<ITypeBinding, ITypeBinding>> typePairs =
      Collections.synchronizedSet(new HashSet<>());;

  private static final int POOL_SIZE = 4;
  private final ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);

  private final FieldWritingBindingResolver assignmentBindingResolver;
  private final FieldWritingsDerivator derivator;

  private final DerivationJobValidator derivationJobValidator = new DerivationJobValidator();


  private static final Logger logger =
      Logger.getLogger(DerivationManager.class.getSimpleName());

  public DerivationManager(FieldWritingBindingResolver assignmentBindingResolver,
      JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.assignmentBindingResolver = assignmentBindingResolver;
    this.derivator = new FieldWritingsDerivator(javaEntitySearcher, codeParser);
  }

  @Override
  public Set<Pair<ITypeBinding, ITypeBinding>> derive(
      List<Pair<FieldWriting, FieldWriting>> input) {

    writingPairs.addAll(input);

    List<Future<DerivationResult>> futures;

    while (!writingPairs.isEmpty()) {
      writingPairs.removeAll(derived);

      try {
        futures = pool.invokeAll(writingPairs.parallelStream()
            .filter(p -> derivationJobValidator.isValid(p))
            .map(p -> new DerivationJob(assignmentBindingResolver, derivator, p))
            .toList());
      } catch (InterruptedException e) {
        e.printStackTrace();

        return typePairs;
      }

      derived.addAll(writingPairs);
      writingPairs.clear();

      for (Future<DerivationResult> f : futures) {
        DerivationResult result;
        try {
          result = f.get();
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();

          return typePairs;
        }

        if (result instanceof NewWritingPairs nwp) {
          writingPairs.addAll(nwp.writingPairs());
        } else if (result instanceof ResolvedBindings rb) {
          var bindingPair = rb.bindingsPair();
          typePairs.add(bindingPair);
        } else {
          logger.severe(
              "Unknown instance of DerivationResult. Impossible to happen! Result: " + result);
        }
      }
    }
    return typePairs;
  }
}

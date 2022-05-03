package upt.ac.cti.coverage.derivator;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;
import upt.ac.cti.coverage.derivator.internal.FieldWritingsDerivator;
import upt.ac.cti.coverage.model.NewWritingPairs;
import upt.ac.cti.coverage.model.ResolvedBindings;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.parsing.CodeParser;
import upt.ac.cti.coverage.search.JavaEntitySearcher;
import upt.ac.cti.coverage.util.FieldWritingBindingResolver;

public class DerivationManager {

  private final LinkedBlockingQueue<Pair<Writing, Writing>> writingPairs =
      new LinkedBlockingQueue<>();
  private final Set<Pair<Writing, Writing>> derived =
      Collections.synchronizedSet(new HashSet<>());
  private final Set<Pair<ITypeBinding, ITypeBinding>> typePairs =
      Collections.synchronizedSet(new HashSet<>());;

  private final FieldWritingBindingResolver assignmentBindingResolver;
  private final FieldWritingsDerivator derivator;

  private final DerivationJobValidator derivationJobValidator = new DerivationJobValidator();

  public DerivationManager(FieldWritingBindingResolver assignmentBindingResolver,
      JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.assignmentBindingResolver = assignmentBindingResolver;
    this.derivator = new FieldWritingsDerivator(javaEntitySearcher, codeParser);
  }

  public Set<Pair<ITypeBinding, ITypeBinding>> derive(
      List<Pair<Writing, Writing>> input) {

    writingPairs.addAll(input);

    while (!writingPairs.isEmpty()) {
      writingPairs.removeAll(derived);

      writingPairs.parallelStream()
          .filter(p -> derivationJobValidator.isValid(p))
          .map(p -> new DerivationJob(assignmentBindingResolver, derivator, p))
          .map(DerivationJob::start)
          .forEach(result -> {
            if (result instanceof NewWritingPairs nwp) {
              writingPairs.addAll(nwp.writingPairs());
            } else if (result instanceof ResolvedBindings rb) {
              var bindingPair = rb.bindingsPair();
              typePairs.add(bindingPair);
            }
          });

      derived.addAll(writingPairs);
    }

    return typePairs;
  }
}

package upt.ac.cti.coverage.derivator;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.aperture.AAllTypePairsResolver;
import upt.ac.cti.coverage.derivator.derivation.simple.SimpleWritingsDerivator;
import upt.ac.cti.coverage.derivator.util.WritingBindingResolver;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.AllTypes;
import upt.ac.cti.coverage.model.derivation.DerivationResult;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;
import upt.ac.cti.coverage.model.derivation.ResolvedTypePairs;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public class DerivationManager<J extends IJavaElement> implements IDerivationManager<J> {

  private static final int DEPTH_THRESHOLD = 3;

  private final LinkedBlockingQueue<Pair<Writing<J>, Writing<J>>> writingPairs =
      new LinkedBlockingQueue<>();
  private final Set<Pair<Writing<J>, Writing<J>>> derived =
      Collections.synchronizedSet(new HashSet<>());
  private final Set<Pair<IType, IType>> typePairs =
      Collections.synchronizedSet(new HashSet<>());;

  private final WritingBindingResolver<J> writingBindingResolver;
  private final SimpleWritingsDerivator<J> derivator;
  private final AAllTypePairsResolver<J> aAllTypePairsResolver;

  public DerivationManager(
      WritingBindingResolver<J> writingBindingResolver,
      JavaEntitySearcher javaEntitySearcher,
      CodeParser codeParser,
      AAllTypePairsResolver<J> aAllTypePairsResolver) {
    this.writingBindingResolver = writingBindingResolver;
    this.derivator = new SimpleWritingsDerivator<>(javaEntitySearcher, codeParser);
    this.aAllTypePairsResolver = aAllTypePairsResolver;
  }

  @Override
  public Set<Pair<IType, IType>> derive(List<Pair<Writing<J>, Writing<J>>> input) {

    writingPairs.addAll(input);


    while (!writingPairs.isEmpty()) {

      var allExcededDepth = writingPairs.parallelStream()
          .allMatch(p -> p.getValue0().depth() >= DEPTH_THRESHOLD &&
              p.getValue1().depth() >= DEPTH_THRESHOLD);

      if (allExcededDepth) {
        var j1 = writingPairs.peek().getValue0().element();
        var j2 = writingPairs.peek().getValue1().element();

        return aAllTypePairsResolver.resolve(j1, j2);
      }

      var results = writingPairs.stream()
          .map(
              p -> new DerivationJob<>(writingBindingResolver, derivator, aAllTypePairsResolver, p))
          .map(DerivationJob::derive)
          .toList();

      var anyIsAllTypes = results.stream().anyMatch(r -> r instanceof AllTypes<J>);

      if (anyIsAllTypes) {
        var j1 = writingPairs.peek().getValue0().element();
        var j2 = writingPairs.peek().getValue1().element();

        return aAllTypePairsResolver.resolve(j1, j2);
      }

      derived.addAll(writingPairs);

      for (DerivationResult<J> r : results) {
        if (r instanceof NewWritingPairs<J> nwp) {
          writingPairs.addAll(nwp.writingPairs());
        } else if (r instanceof ResolvedTypePairs<J> rtp) {
          typePairs.addAll(rtp.typePairs());
        } else if (r instanceof AllTypes<J>) {

        }
      }

      writingPairs.removeAll(derived);
    }

    return typePairs;
  }

}

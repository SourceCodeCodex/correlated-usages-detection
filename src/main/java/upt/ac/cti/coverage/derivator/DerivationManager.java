package upt.ac.cti.coverage.derivator;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.aperture.AAllTypePairsResolver;
import upt.ac.cti.config.Config;
import upt.ac.cti.coverage.derivator.derivation.complex.ComplexWritingsDerivator;
import upt.ac.cti.coverage.derivator.derivation.simple.SimpleWritingsDerivator;
import upt.ac.cti.coverage.derivator.util.AWritingBindingResolver;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.DerivationResult;
import upt.ac.cti.coverage.model.derivation.Inconclusive;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;
import upt.ac.cti.coverage.model.derivation.ResolvedTypePairs;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public class DerivationManager<J extends IJavaElement> implements IDerivationManager<J> {

  private static final int DEPTH_THRESHOLD = Config.MAX_DEPTH_THRESHOLD;
  private static final int DEPTH_DIFF = Config.MAX_DEPTH_DIFF;

  private final LinkedBlockingQueue<Pair<Writing<J>, Writing<J>>> writingPairs =
      new LinkedBlockingQueue<>();
  private final Set<Pair<Writing<J>, Writing<J>>> derived =
      Collections.synchronizedSet(new HashSet<>());
  private final Set<Pair<IType, IType>> typePairs = Collections.synchronizedSet(new HashSet<>());;

  private final AWritingBindingResolver<J> writingBindingResolver;
  private final SimpleWritingsDerivator<J> simpleDerivator;
  private final ComplexWritingsDerivator<J> complexDerivator;
  private final AAllTypePairsResolver<J> aAllTypePairsResolver;

  public DerivationManager(AWritingBindingResolver<J> writingBindingResolver,
      JavaEntitySearcher javaEntitySearcher,
      CodeParser codeParser, AAllTypePairsResolver<J> aAllTypePairsResolver) {
    this.writingBindingResolver = writingBindingResolver;
    this.simpleDerivator = new SimpleWritingsDerivator<>(javaEntitySearcher, codeParser);
    this.complexDerivator = new ComplexWritingsDerivator<>(javaEntitySearcher, codeParser);
    this.aAllTypePairsResolver = aAllTypePairsResolver;
  }

  @Override
  public Optional<Set<Pair<IType, IType>>> derive(List<Pair<Writing<J>, Writing<J>>> input) {

    writingPairs.addAll(input);

    while (!writingPairs.isEmpty()) {

      var allExcededDepth = writingPairs.stream().allMatch(this::isAboveThreshold);

      if (allExcededDepth) {
        return Optional.of(typePairs);
      }

      var results = writingPairs.parallelStream().filter(this::isBelowThreshold)
          .map(p -> new DerivationJob<>(writingBindingResolver, simpleDerivator, complexDerivator,
              aAllTypePairsResolver, p))
          .map(DerivationJob::derive).toList();

      var anyIsInconclusive = results.stream().anyMatch(r -> r instanceof Inconclusive<J>);

      if (anyIsInconclusive) {
        return Optional.empty();
      }

      derived.addAll(writingPairs);

      for (DerivationResult<J> r : results) {
        if (r instanceof NewWritingPairs<J> nwp) {
          writingPairs.addAll(nwp.writingPairs());
        } else if (r instanceof ResolvedTypePairs<J> rtp) {
          typePairs.addAll(rtp.typePairs());
        }
      }

      writingPairs.removeAll(derived);
    }

    return Optional.of(typePairs);
  }

  private boolean isAboveThreshold(Pair<Writing<J>, Writing<J>> p) {
    return p.getValue0().depth() > DEPTH_THRESHOLD && p.getValue1().depth() > DEPTH_THRESHOLD;
  }

  private boolean isBelowThreshold(Pair<Writing<J>, Writing<J>> p) {
    return p.getValue0().depth() <= DEPTH_THRESHOLD && p.getValue1().depth() <= DEPTH_THRESHOLD
        && Math.abs(p.getValue0().depth() - p.getValue1().depth()) <= DEPTH_DIFF;
  }

}

package upt.ac.cti.coverage.flow_insensitive.derivator;

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
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex.ComplexWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.simple.SimpleWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.util.ADerivableWritingBindingResolver;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.DerivationResult;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.Inconclusive;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.ResolvedTypePairs;

class FullDepthDerivationManager<J extends IJavaElement> implements IDerivationManager<J> {

  private final ADerivableWritingBindingResolver<J> writingBindingResolver;
  private final AAllTypePairsResolver<J> aAllTypePairsResolver;

  private final SimpleWritingsDerivator<J> simpleDerivator;
  private final ComplexWritingsDerivator<J> complexDerivator;

  public FullDepthDerivationManager(ADerivableWritingBindingResolver<J> writingBindingResolver,
      AAllTypePairsResolver<J> aAllTypePairsResolver) {
    this.writingBindingResolver = writingBindingResolver;
    this.aAllTypePairsResolver = aAllTypePairsResolver;
    this.simpleDerivator = new SimpleWritingsDerivator<>();
    this.complexDerivator = new ComplexWritingsDerivator<>();
  }

  protected boolean isAboveThreshold(Pair<? extends Writing<J>, ? extends Writing<J>> p) {
    return p.getValue0().depth() > Config.MAX_DEPTH_THRESHOLD
        || p.getValue1().depth() > Config.MAX_DEPTH_THRESHOLD;
  }

  protected void preprocessPairsDepth(
      LinkedBlockingQueue<Pair<? extends Writing<J>, ? extends Writing<J>>> writingPairs,
      Set<Pair<? extends Writing<J>, ? extends Writing<J>>> derived,
      Set<Pair<IType, IType>> typePairs) {
    // do nothing
    // can be used if depth optimization is not required
  }

  @Override
  public Optional<Set<Pair<IType, IType>>> derive(
      List<Pair<DerivableWriting<J>, DerivableWriting<J>>> input) {

    if (input.isEmpty()) {
      return Optional.of(new HashSet<Pair<IType, IType>>());
    }

    var writingPairs = new LinkedBlockingQueue<Pair<? extends Writing<J>, ? extends Writing<J>>>();
    var derived =
        Collections
            .synchronizedSet(new HashSet<Pair<? extends Writing<J>, ? extends Writing<J>>>());
    var typePairs = Collections.synchronizedSet(new HashSet<Pair<IType, IType>>());

    var first = input.get(0);
    var aperture = aAllTypePairsResolver
        .resolve(first.getValue0().element(), first.getValue1().element()).size();

    writingPairs.addAll(input);

    while (!writingPairs.isEmpty()) {

      preprocessPairsDepth(writingPairs, derived, typePairs);

      if (typePairs.size() == aperture) {
        return Optional.of(typePairs);
      }

      var results = writingPairs.parallelStream()
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



}

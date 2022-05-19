package upt.ac.cti.coverage.flow_insensitive.derivator;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.aperture.AAllTypePairsResolver;
import upt.ac.cti.coverage.flow_insensitive.derivator.util.ADerivableWritingBindingResolver;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;

public class SquanderingDerivationManager<J extends IJavaElement>
    extends FullDepthDerivationManager<J> {

  public SquanderingDerivationManager(
      ADerivableWritingBindingResolver<J> writingBindingResolver,
      AAllTypePairsResolver<J> aAllTypePairsResolver) {
    super(writingBindingResolver, aAllTypePairsResolver);
  }

  @Override
  protected void preprocessPairsDepth(
      LinkedBlockingQueue<Pair<? extends Writing<J>, ? extends Writing<J>>> writingPairs,
      Set<Pair<? extends Writing<J>, ? extends Writing<J>>> derived,
      Set<Pair<IType, IType>> typePairs) {
    var allExceeded =
        writingPairs
            .stream()
            .filter(p -> p.getValue0() instanceof DerivableWriting<?>
                && p.getValue1() instanceof DerivableWriting<?>)
            .filter(this::isAboveThreshold)
            .toList();

    writingPairs.removeAll(allExceeded);
    derived.addAll(allExceeded);
  }
}

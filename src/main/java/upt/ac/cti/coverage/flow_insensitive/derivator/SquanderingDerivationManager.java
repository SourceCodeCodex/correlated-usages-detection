package upt.ac.cti.coverage.flow_insensitive.derivator;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.aperture.AAllTypePairsResolver;
import upt.ac.cti.coverage.flow_insensitive.derivator.util.AWritingBindingResolver;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;

public class SquanderingDerivationManager<J extends IJavaElement> extends FullDerivationManager<J> {

  public SquanderingDerivationManager(
      AWritingBindingResolver<J> writingBindingResolver,
      AAllTypePairsResolver<J> aAllTypePairsResolver) {
    super(writingBindingResolver, aAllTypePairsResolver);
  }

  @Override
  protected void preprocessPairsDepth(
      LinkedBlockingQueue<Pair<Writing<J>, Writing<J>>> writingPairs,
      Set<Pair<Writing<J>, Writing<J>>> derived,
      Set<Pair<IType, IType>> typePairs) {
    var allExceeded = writingPairs.stream().filter(this::isAboveThreshold).toList();

    writingPairs.removeAll(allExceeded);
    derived.addAll(allExceeded);
  }
}

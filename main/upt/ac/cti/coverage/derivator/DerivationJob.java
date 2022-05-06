package upt.ac.cti.coverage.derivator;

import java.util.logging.Logger;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.aperture.AAllTypePairsResolver;
import upt.ac.cti.coverage.derivator.derivation.simple.SimpleWritingsDerivator;
import upt.ac.cti.coverage.derivator.util.DerivationPrioritizer;
import upt.ac.cti.coverage.derivator.util.SameAccessExpressionValidator;
import upt.ac.cti.coverage.derivator.util.WritingBindingResolver;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.binding.Inconclusive;
import upt.ac.cti.coverage.model.binding.NotLeafBinding;
import upt.ac.cti.coverage.model.binding.ResolvedBinding;
import upt.ac.cti.coverage.model.derivation.AllTypes;
import upt.ac.cti.coverage.model.derivation.DerivationResult;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;
import upt.ac.cti.coverage.model.derivation.ResolvedTypePairs;
import upt.ac.cti.util.logging.RLogger;

class DerivationJob<J extends IJavaElement> {

  private final Pair<Writing<J>, Writing<J>> writingPair;

  private final DerivationPrioritizer<J> derivationPrioritizer = new DerivationPrioritizer<>();
  private final SameAccessExpressionValidator<J> sameAccessExpressionValidator =
      new SameAccessExpressionValidator<>();
  private final WritingBindingResolver<J> assignmentBindingResolver;
  private final SimpleWritingsDerivator<J> simpleDerivator;
  private final AAllTypePairsResolver<J> aAllTypePairsResolver;

  private static final Logger logger = RLogger.get();

  public DerivationJob(
      WritingBindingResolver<J> assignmentBindingResolver,
      SimpleWritingsDerivator<J> derivator,
      AAllTypePairsResolver<J> aAllTypePairsResolver,
      Pair<Writing<J>, Writing<J>> writingPair) {
    this.assignmentBindingResolver = assignmentBindingResolver;
    this.simpleDerivator = derivator;
    this.writingPair = writingPair;
    this.aAllTypePairsResolver = aAllTypePairsResolver;
  }

  public DerivationResult<J> derive() {

    var w1 = writingPair.getValue0();
    var w2 = writingPair.getValue1();

    var w1Binding =
        assignmentBindingResolver.resolveBinding(w1);

    var w2Binding =
        assignmentBindingResolver.resolveBinding(writingPair.getValue1());

    if (w1Binding instanceof ResolvedBinding r1 && w2Binding instanceof ResolvedBinding r2) {
      if (sameAccessExpressionValidator.test(writingPair)) {
        var t1 = (IType) r1.typeBinding().getJavaElement();
        var t2 = (IType) r2.typeBinding().getJavaElement();
        return ResolvedTypePairs.of(Pair.with(t1, t2));
      }
      return NewWritingPairs.NULL();
    }

    if (w1Binding instanceof Inconclusive && w2Binding instanceof Inconclusive) {
      logger.info("Inconclusive field pair: " + writingPair);
      return new AllTypes<>();
    }

    if (w1Binding instanceof ResolvedBinding r && w2Binding instanceof Inconclusive) {
      var t = (IType) r.typeBinding().getJavaElement();
      var possibleTypes = aAllTypePairsResolver.resolve(w2.element());
      var pairs = possibleTypes.stream().map(pt -> Pair.with(t, pt)).toList();
      return new ResolvedTypePairs<>(pairs);
    }

    if (w1Binding instanceof Inconclusive && w2Binding instanceof ResolvedBinding r) {
      var t = (IType) r.typeBinding().getJavaElement();
      var possibleTypes = aAllTypePairsResolver.resolve(w1.element());
      var pairs = possibleTypes.stream().map(pt -> Pair.with(pt, t)).toList();
      return new ResolvedTypePairs<>(pairs);
    }

    if (w1Binding instanceof NotLeafBinding && !(w2Binding instanceof NotLeafBinding)) {
      return simpleDerivator.derive(w1, w2);
    }

    if (!(w1Binding instanceof NotLeafBinding) && w2Binding instanceof NotLeafBinding) {
      return swap(simpleDerivator.derive(w2, w1));
    }

    var priority = derivationPrioritizer.prioritize(writingPair);
    switch (priority) {
      case DERIVATE_FIRST:
        return simpleDerivator.derive(w1, w2);
      case DERIVATE_SECOND:
        return swap(simpleDerivator.derive(w2, w1));
      default:
        return NewWritingPairs.NULL();
    }
  }

  private NewWritingPairs<J> swap(NewWritingPairs<J> pairings) {
    return new NewWritingPairs<>(pairings.writingPairs().stream()
        .map(p -> Pair.with(p.getValue1(), p.getValue0()))
        .toList());
  }

}

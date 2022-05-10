package upt.ac.cti.coverage.derivator;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.aperture.AAllTypePairsResolver;
import upt.ac.cti.coverage.derivator.derivation.complex.ComplexWritingsDerivator;
import upt.ac.cti.coverage.derivator.derivation.simple.SimpleWritingsDerivator;
import upt.ac.cti.coverage.derivator.util.DerivationPrioritizer;
import upt.ac.cti.coverage.derivator.util.WritingBindingResolver;
import upt.ac.cti.coverage.derivator.util.access.SameAccessExpressionValidator;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.binding.Inconclusive;
import upt.ac.cti.coverage.model.binding.NotLeafBinding;
import upt.ac.cti.coverage.model.binding.ResolvedBinding;
import upt.ac.cti.coverage.model.derivation.DerivationResult;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;
import upt.ac.cti.coverage.model.derivation.ResolvedTypePairs;

class DerivationJob<J extends IJavaElement> {

  private final Pair<Writing<J>, Writing<J>> writingPair;

  private final DerivationPrioritizer<J> derivationPrioritizer = new DerivationPrioritizer<>();
  private final SameAccessExpressionValidator<J> sameAccessExpressionValidator =
      new SameAccessExpressionValidator<>();
  private final WritingBindingResolver<J> assignmentBindingResolver;
  private final SimpleWritingsDerivator<J> simpleDerivator;
  private final ComplexWritingsDerivator<J> complexDerivator;
  private final AAllTypePairsResolver<J> aAllTypePairsResolver;

  public DerivationJob(
      WritingBindingResolver<J> assignmentBindingResolver,
      SimpleWritingsDerivator<J> simpleDerivator,
      ComplexWritingsDerivator<J> complexDerivator,
      AAllTypePairsResolver<J> aAllTypePairsResolver,
      Pair<Writing<J>, Writing<J>> writingPair) {
    this.assignmentBindingResolver = assignmentBindingResolver;
    this.simpleDerivator = simpleDerivator;
    this.complexDerivator = complexDerivator;
    this.writingPair = writingPair;
    this.aAllTypePairsResolver = aAllTypePairsResolver;
  }

  public DerivationResult<J> derive() {

    var w1 = writingPair.getValue0();
    var w2 = writingPair.getValue1();

    var w1Binding =

        assignmentBindingResolver.resolveBinding(w1);

    var w2Binding =
        assignmentBindingResolver.resolveBinding(w2);

    if (w1Binding instanceof ResolvedBinding r1 && w2Binding instanceof ResolvedBinding r2) {
      if (sameAccessExpressionValidator.test(writingPair)) {
        var t1 = r1.typeBinding().getJavaElement();
        var t2 = r2.typeBinding().getJavaElement();
        if (t1 instanceof IType it1 && t2 instanceof IType it2) {
          return ResolvedTypePairs.of(Pair.with(it1, it2));
        }
      }
      return NewWritingPairs.NULL();
    }

    if (w1Binding instanceof Inconclusive && w2Binding instanceof Inconclusive) {
      return new upt.ac.cti.coverage.model.derivation.Inconclusive<>();
    }

    if (w1Binding instanceof ResolvedBinding r && w2Binding instanceof Inconclusive) {
      if (r.typeBinding().getJavaElement() instanceof IType t) {
        var possibleTypes = aAllTypePairsResolver.resolve(w2.element());
        var pairs = possibleTypes.stream().map(pt -> Pair.with(t, pt)).toList();
        return new ResolvedTypePairs<>(pairs);
      }
      return NewWritingPairs.NULL();
    }

    if (w1Binding instanceof Inconclusive && w2Binding instanceof ResolvedBinding r) {
      if (r.typeBinding().getJavaElement() instanceof IType t) {
        var possibleTypes = aAllTypePairsResolver.resolve(w1.element());
        var pairs = possibleTypes.stream().map(pt -> Pair.with(pt, t)).toList();
        return new ResolvedTypePairs<>(pairs);
      }
      return NewWritingPairs.NULL();
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
        return complexDerivator.derive(w1, w2);
    }
  }

  private NewWritingPairs<J> swap(NewWritingPairs<J> pairings) {
    return new NewWritingPairs<>(pairings.writingPairs().stream()
        .map(p -> Pair.with(p.getValue1(), p.getValue0()))
        .toList());
  }

}

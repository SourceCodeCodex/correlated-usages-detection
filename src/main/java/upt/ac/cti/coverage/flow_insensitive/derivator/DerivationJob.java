package upt.ac.cti.coverage.flow_insensitive.derivator;

import java.util.List;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.aperture.AAllTypePairsResolver;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex.ComplexWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.simple.SimpleWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.util.ADerivableWritingBindingResolver;
import upt.ac.cti.coverage.flow_insensitive.derivator.util.DerivationPrioritizer;
import upt.ac.cti.coverage.flow_insensitive.derivator.util.access.SameAccessExpressionValidator;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.UnderivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.binding.Inconclusive;
import upt.ac.cti.coverage.flow_insensitive.model.binding.NotLeafBinding;
import upt.ac.cti.coverage.flow_insensitive.model.binding.ResolvedBinding;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.DerivationResult;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.ResolvedTypePairs;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.computation.CartesianProduct;
import upt.ac.cti.util.hierarchy.HierarchyResolver;

class DerivationJob<J extends IJavaElement> {

  private final Pair<? extends Writing<J>, ? extends Writing<J>> writingPair;

  private final DerivationPrioritizer<J> derivationPrioritizer = new DerivationPrioritizer<>();
  private final HierarchyResolver hierarchyResolver = Dependencies.hierarchyResolver;
  private final SameAccessExpressionValidator<J> sameAccessExpressionValidator =
      new SameAccessExpressionValidator<>();
  private final ADerivableWritingBindingResolver<J> assignmentBindingResolver;
  private final SimpleWritingsDerivator<J> simpleDerivator;
  private final ComplexWritingsDerivator<J> complexDerivator;
  private final AAllTypePairsResolver<J> aAllTypePairsResolver;

  public DerivationJob(
      ADerivableWritingBindingResolver<J> assignmentBindingResolver,
      SimpleWritingsDerivator<J> simpleDerivator,
      ComplexWritingsDerivator<J> complexDerivator,
      AAllTypePairsResolver<J> aAllTypePairsResolver,
      Pair<? extends Writing<J>, ? extends Writing<J>> writingPair) {
    this.assignmentBindingResolver = assignmentBindingResolver;
    this.simpleDerivator = simpleDerivator;
    this.complexDerivator = complexDerivator;
    this.writingPair = writingPair;
    this.aAllTypePairsResolver = aAllTypePairsResolver;
  }

  @SuppressWarnings("unchecked")
  public DerivationResult<J> derive() {

    if (writingPair.getValue0() instanceof UnderivableWriting<?>
        && writingPair.getValue1() instanceof UnderivableWriting<?>) {
      return handleUU();
    }

    if (writingPair.getValue0() instanceof DerivableWriting<?>
        && writingPair.getValue1() instanceof UnderivableWriting<?>) {
      var dw = (DerivableWriting<J>) writingPair.getValue0();
      var uw = (UnderivableWriting<J>) writingPair.getValue1();
      return handleDU(dw, uw);
    }


    if (writingPair.getValue1() instanceof DerivableWriting<?>
        && writingPair.getValue0() instanceof UnderivableWriting<?>) {
      var dw = (DerivableWriting<J>) writingPair.getValue1();
      var uw = (UnderivableWriting<J>) writingPair.getValue0();
      var r = handleDU(dw, uw);
      if (r instanceof NewWritingPairs<J> nwp) {
        return swap(nwp);
      }
      return r;
    }

    return handleDD();
  }


  @SuppressWarnings("unchecked")
  private DerivationResult<J> handleUU() {
    var uw1 = (UnderivableWriting<J>) writingPair.getValue0();
    var uw2 = (UnderivableWriting<J>) writingPair.getValue1();

    var t1 = (IType) uw1.writingExpression().resolveTypeBinding().getJavaElement();
    var t2 = (IType) uw2.writingExpression().resolveTypeBinding().getJavaElement();

    var product = CartesianProduct.product(hierarchyResolver.resolveConcrete(t1),
        hierarchyResolver.resolveConcrete(t2));

    return new ResolvedTypePairs<>(product);
  }

  private DerivationResult<J> handleDU(DerivableWriting<J> dw, UnderivableWriting<J> uw) {

    var dwBinding =
        assignmentBindingResolver.resolveBinding(dw);

    var ut = (IType) uw.writingExpression().resolveTypeBinding().getJavaElement();

    if (dwBinding instanceof Inconclusive) {
      var dwTypes = aAllTypePairsResolver.resolve(dw.element());
      var utTypes = hierarchyResolver.resolveConcrete(ut);
      return new ResolvedTypePairs<>(CartesianProduct.product(dwTypes, utTypes));
    }

    if (dwBinding instanceof ResolvedBinding r) {
      if (r.typeBinding().getJavaElement() instanceof IType t) {
        var dwTypes = List.of(t);
        var utTypes = hierarchyResolver.resolveConcrete(ut);
        return new ResolvedTypePairs<>(CartesianProduct.product(dwTypes, utTypes));
      }
      return NewWritingPairs.NULL();
    }

    return simpleDerivator.derive(dw, uw);

  }

  @SuppressWarnings("unchecked")
  private DerivationResult<J> handleDD() {
    var dw1 = (DerivableWriting<J>) writingPair.getValue0();
    var dw2 = (DerivableWriting<J>) writingPair.getValue1();

    var w1Binding =
        assignmentBindingResolver.resolveBinding(dw1);

    var w2Binding =
        assignmentBindingResolver.resolveBinding(dw2);

    if (w1Binding instanceof ResolvedBinding r1 && w2Binding instanceof ResolvedBinding r2) {
      if (sameAccessExpressionValidator.test(Pair.with(dw1, dw2))) {
        var t1 = r1.typeBinding().getJavaElement();
        var t2 = r2.typeBinding().getJavaElement();
        if (t1 instanceof IType it1 && t2 instanceof IType it2) {
          return ResolvedTypePairs.of(Pair.with(it1, it2));
        }
      }
      return NewWritingPairs.NULL();
    }

    if (w1Binding instanceof Inconclusive && w2Binding instanceof Inconclusive) {
      return new upt.ac.cti.coverage.flow_insensitive.model.derivation.Inconclusive<>();
    }

    if (w1Binding instanceof ResolvedBinding r && w2Binding instanceof Inconclusive) {
      if (r.typeBinding().getJavaElement() instanceof IType t) {
        var possibleTypes = aAllTypePairsResolver.resolve(dw2.element());
        var pairs = possibleTypes.stream().map(pt -> Pair.with(t, pt)).toList();
        return new ResolvedTypePairs<>(pairs);
      }
      return NewWritingPairs.NULL();
    }

    if (w1Binding instanceof Inconclusive && w2Binding instanceof ResolvedBinding r) {
      if (r.typeBinding().getJavaElement() instanceof IType t) {
        var possibleTypes = aAllTypePairsResolver.resolve(dw1.element());
        var pairs = possibleTypes.stream().map(pt -> Pair.with(pt, t)).toList();
        return new ResolvedTypePairs<>(pairs);
      }
      return NewWritingPairs.NULL();
    }

    if (w1Binding instanceof NotLeafBinding && !(w2Binding instanceof NotLeafBinding)) {
      return simpleDerivator.derive(dw1, dw2);
    }

    if (!(w1Binding instanceof NotLeafBinding) && w2Binding instanceof NotLeafBinding) {
      return swap(simpleDerivator.derive(dw2, dw1));
    }

    // FROM HERE, ANY GET ON WRITING EXPRESSIONS IS SAFE
    var priority = derivationPrioritizer.prioritize(Pair.with(dw1, dw2));
    switch (priority) {
      case DERIVATE_FIRST:
        return simpleDerivator.derive(dw1, dw2);
      case DERIVATE_SECOND:
        return swap(simpleDerivator.derive(dw2, dw1));
      default:
        return complexDerivator.derive(dw1, dw2);
    }
  }



  private NewWritingPairs<J> swap(NewWritingPairs<J> pairings) {
    return new NewWritingPairs<>(pairings.writingPairs().stream()
        .map(p -> Pair.with(p.getValue1(), p.getValue0()))
        .toList());
  }


}

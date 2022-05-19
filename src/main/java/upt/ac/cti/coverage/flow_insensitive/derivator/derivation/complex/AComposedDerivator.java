package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import org.javatuples.Pair;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.IComplexWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;
import upt.ac.cti.util.computation.CartesianProduct;

abstract class AComposedDerivator<J extends IJavaElement> implements IComplexWritingsDerivator<J> {

  private final IEntityDerivator<J> ed1;
  private final IEntityDerivator<J> ed2;

  public AComposedDerivator(IEntityDerivator<J> ed1, IEntityDerivator<J> ed2) {
    this.ed1 = ed1;
    this.ed2 = ed2;
  }


  @Override
  public NewWritingPairs<J> derive(DerivableWriting<J> w1, DerivableWriting<J> w2) {

    var w1Derivations = ed1.derive(w1).writingPairs().stream()
        .map(Pair::getValue0)
        .toList();
    var w2Derivations = ed2.derive(w2).writingPairs().stream()
        .map(Pair::getValue0)
        .toList();

    var complexDerivation = CartesianProduct.productE(w1Derivations, w2Derivations);

    return new NewWritingPairs<>(complexDerivation);
  }

}

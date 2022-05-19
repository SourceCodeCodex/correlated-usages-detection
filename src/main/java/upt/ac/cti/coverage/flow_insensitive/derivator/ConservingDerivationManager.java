package upt.ac.cti.coverage.flow_insensitive.derivator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.aperture.AAllTypePairsResolver;
import upt.ac.cti.coverage.flow_insensitive.derivator.util.ADerivableWritingBindingResolver;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.UnderivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.binding.NotLeafBinding;
import upt.ac.cti.coverage.flow_insensitive.model.binding.ResolvedBinding;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.computation.CartesianProduct;
import upt.ac.cti.util.hierarchy.HierarchyResolver;

public class ConservingDerivationManager<J extends IJavaElement>
    extends FullDepthDerivationManager<J> {

  private final HierarchyResolver hierarchyResolver = Dependencies.hierarchyResolver;

  private final ADerivableWritingBindingResolver<J> writingBindingResolver;
  private final AAllTypePairsResolver<J> allTypePairsResolver;

  public ConservingDerivationManager(
      ADerivableWritingBindingResolver<J> writingBindingResolver,
      AAllTypePairsResolver<J> aAllTypePairsResolver) {
    super(writingBindingResolver, aAllTypePairsResolver);
    this.writingBindingResolver = writingBindingResolver;
    this.allTypePairsResolver = aAllTypePairsResolver;
  }

  @Override
  protected void preprocessPairsDepth(
      LinkedBlockingQueue<Pair<? extends Writing<J>, ? extends Writing<J>>> writingPairs,
      Set<Pair<? extends Writing<J>, ? extends Writing<J>>> derived,
      Set<Pair<IType, IType>> typePairs) {

    var allExceeded = writingPairs.stream().filter(this::isAboveThreshold).toList();

    writingPairs.removeAll(allExceeded);
    derived.addAll(allExceeded);

    var conserved = allExceeded.stream()
        .map(p -> Pair.with((Writing<J>) p.getValue0(), (Writing<J>) p.getValue1()))
        .flatMap(p -> CartesianProduct
            .product(possibleTypes(p.getValue0()), possibleTypes(p.getValue1())).stream())
        .toList();

    typePairs.addAll(conserved);

  }

  private List<IType> possibleTypes(Writing<J> w) {
    if (w instanceof UnderivableWriting<J> u) {
      return possibleTypesU(u);
    }

    return possibleTypesD((DerivableWriting<J>) w);
  }

  private List<IType> possibleTypesU(UnderivableWriting<J> w) {
    var t = w.writingExpression().resolveTypeBinding().getJavaElement();
    return hierarchyResolver.resolveConcrete((IType) t);
  }

  private List<IType> possibleTypesD(DerivableWriting<J> w) {
    var b = writingBindingResolver.resolveBinding(w);

    if (b instanceof ResolvedBinding rb) {
      var t = rb.typeBinding().getJavaElement();
      if (t == null || !(t instanceof IType)) {
        return allTypePairsResolver.resolve(w.element());
      }
      return List.of((IType) t);
    }

    if (b instanceof NotLeafBinding nl) {
      var t = nl.typeBinding().getJavaElement();
      if (t == null || !(t instanceof IType)) {
        return allTypePairsResolver.resolve(w.element());
      }
      return hierarchyResolver.resolveConcrete((IType) t);
    }

    return allTypePairsResolver.resolve(w.element());


  }

}

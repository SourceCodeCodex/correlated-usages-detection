package upt.ac.cti.coverage;

import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;
import upt.ac.cti.aperture.AAllTypePairsResolver;
import upt.ac.cti.coverage.combiner.IWritingsCombiner;
import upt.ac.cti.coverage.derivator.DerivationManager;
import upt.ac.cti.coverage.derivator.util.WritingBindingResolver;
import upt.ac.cti.util.binding.ABindingResolver;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

abstract class ACoveredTypesResolver<J extends IJavaElement> {

  private final CodeParser codeParser;
  private final JavaEntitySearcher javaEntitySearcher;
  private final ABindingResolver<J, ITypeBinding> aBindingResolver;
  private final IWritingsCombiner<J> writingsCombiner;
  private final AAllTypePairsResolver<J> aAllTypePairsResolver;


  public ACoveredTypesResolver(
      IWritingsCombiner<J> writingsCombiner,
      CodeParser codeParser,
      JavaEntitySearcher javaEntitySearcher,
      ABindingResolver<J, ITypeBinding> aBindingResolver,
      AAllTypePairsResolver<J> aAllTypePairsResolver) {
    this.aBindingResolver = aBindingResolver;
    this.writingsCombiner = writingsCombiner;
    this.codeParser = codeParser;
    this.javaEntitySearcher = javaEntitySearcher;
    this.aAllTypePairsResolver = aAllTypePairsResolver;
  }

  protected Optional<Set<Pair<IType, IType>>> resolve(J javaElement1, J javaElement2) {

    var writingPairs = writingsCombiner.combine(javaElement1, javaElement2);

    var concreteDescendantsResolver = new ConcreteDescendantsResolver();
    var assignmentBindingResolver =
        new WritingBindingResolver<>(concreteDescendantsResolver, aBindingResolver);

    var deriver =
        new DerivationManager<>(
            assignmentBindingResolver,
            javaEntitySearcher,
            codeParser,
            aAllTypePairsResolver);

    return deriver.derive(writingPairs);
  }

}

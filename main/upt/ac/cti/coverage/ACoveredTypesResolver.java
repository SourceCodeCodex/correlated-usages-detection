package upt.ac.cti.coverage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.coverage.derivator.DerivationManager;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.parsing.CodeParser;
import upt.ac.cti.coverage.search.JavaEntitySearcher;
import upt.ac.cti.coverage.util.FieldWritingBindingResolver;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;

abstract class ACoveredTypesResolver {

  protected final CodeParser codeParser;
  protected final JavaEntitySearcher javaEntitySearcher;

  public ACoveredTypesResolver(CodeParser codeParser, JavaEntitySearcher javaEntitySearcher) {
    this.codeParser = codeParser;
    this.javaEntitySearcher = javaEntitySearcher;
  }

  protected abstract List<Pair<Writing, Writing>> writingPairs(IJavaElement javaElement1,
      IJavaElement javaElement2);

  protected Set<Pair<IType, IType>> resolve(IJavaElement javaElement1, IJavaElement javaElement2) {

    var writingPairs = writingPairs(javaElement1, javaElement2);

    var hierarchyResolver = new ConcreteDescendantsResolver();
    var assignmentBindingResolver = new FieldWritingBindingResolver(hierarchyResolver);

    var deriver = new DerivationManager(assignmentBindingResolver, javaEntitySearcher, codeParser);

    var bindingPairs = deriver.derive(writingPairs);

    var coveredTypes = bindingPairs.stream()
        .filter(
            p -> p.getValue0().getJavaElement() == null || p.getValue1().getJavaElement() == null)
        .map(
            p -> Pair.with((IType) p.getValue0().getJavaElement(),
                (IType) p.getValue1().getJavaElement()))
        .toList();

    return new HashSet<>(coveredTypes);
  }

}

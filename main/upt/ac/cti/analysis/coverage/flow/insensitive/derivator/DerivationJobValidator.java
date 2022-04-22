package upt.ac.cti.analysis.coverage.flow.insensitive.derivator;

import java.util.List;
import java.util.function.Predicate;
import org.eclipse.jdt.core.dom.ASTNode;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;

class DerivationJobValidator {

  public boolean isValid(Pair<FieldWriting, FieldWriting> wp) {
    return !list().stream().anyMatch(p -> p.test(wp));
  }

  private List<Predicate<Pair<FieldWriting, FieldWriting>>> list() {
    return List.of(
        baseObjectsDifferentRef,
        anyIsNull,
        anyIsArrayAccess);
  }

  // TODO: the condition that objects are different is a NECESSARY condition when computin the
  // result
  // this predicate should make sure the expressions are referencing to the same object
  private final Predicate<Pair<FieldWriting, FieldWriting>> baseObjectsDifferentRef =
      wp -> false;

  private final Predicate<Pair<FieldWriting, FieldWriting>> anyIsNull =
      anyIs(ASTNode.NULL_LITERAL);

  private final Predicate<Pair<FieldWriting, FieldWriting>> anyIsArrayAccess =
      anyIs(ASTNode.ARRAY_ACCESS);

  private Predicate<Pair<FieldWriting, FieldWriting>> anyIs(int astCode) {
    return wp -> wp.getValue0().writingExpression().getNodeType() == astCode
        || wp.getValue1().writingExpression().getNodeType() == astCode;
  }
}

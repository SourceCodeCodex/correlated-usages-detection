package upt.ac.cti.coverage.derivator;

import java.util.List;
import java.util.function.Predicate;
import org.eclipse.jdt.core.dom.ASTNode;
import org.javatuples.Pair;
import upt.ac.cti.coverage.model.Writing;

class DerivationJobValidator {

  public boolean isValid(Pair<Writing, Writing> wp) {
    return !list().stream().anyMatch(p -> p.test(wp));
  }

  private List<Predicate<Pair<Writing, Writing>>> list() {
    return List.of(
        anyIsNull,
        anyIsArrayAccess);
  }

  private final Predicate<Pair<Writing, Writing>> anyIsNull =
      anyIs(ASTNode.NULL_LITERAL);

  private final Predicate<Pair<Writing, Writing>> anyIsArrayAccess =
      anyIs(ASTNode.ARRAY_ACCESS);

  private Predicate<Pair<Writing, Writing>> anyIs(int astCode) {
    return wp -> wp.getValue0().writingExpression().getNodeType() == astCode
        || wp.getValue1().writingExpression().getNodeType() == astCode;
  }
}

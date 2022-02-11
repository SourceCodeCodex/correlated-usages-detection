package upt.ac.cti.coverage.analysis.flow.insensitive.iterators;

import java.util.List;
import java.util.function.Predicate;
import org.eclipse.jdt.core.dom.ASTNode;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CorelationPair;

class CPHandlingInvalidator {

  private CPHandlingInvalidator() {

  }

  public static boolean isInvalid(CorelationPair cp) {
    return list().stream().anyMatch(p -> p.test(cp));
  }


  private static List<Predicate<CorelationPair>> list() {
    return List.of(
        baseObjectsDifferentRef,
        anyIsNull,
        anyIsArrayAccess);
  }

  // TODO: the condition that objects are different is a NECESSARY condition when computin the
  // result
  // this predicate should make sure the expressions are referencing to the same object
  private static final Predicate<CorelationPair> baseObjectsDifferentRef =
      cp -> false;

  private static final Predicate<CorelationPair> anyIsNull = anyIs(ASTNode.NULL_LITERAL);

  private static final Predicate<CorelationPair> anyIsArrayAccess = anyIs(ASTNode.ARRAY_ACCESS);

  private static Predicate<CorelationPair> anyIs(int astCode) {
    return cp -> cp.field1Asgmt().rightSide().getNodeType() == astCode
        || cp.field2Asgmt().rightSide().getNodeType() == astCode;
  }


}

package upt.ac.cti.coverage.analysis.iterative.iterators;

import java.util.List;
import java.util.function.Predicate;
import org.eclipse.jdt.core.dom.ASTNode;
import upt.ac.cti.coverage.analysis.iterative.model.CorelationPair;

class CPHandlingInvalidator {

  private CPHandlingInvalidator() {

  }

  public static boolean isInvalid(CorelationPair cp) {
    return list().stream().anyMatch(p -> p.test(cp));
  }


  public static List<Predicate<CorelationPair>> list() {
    return List.of(
        baseObjectsDifferent,
        anyIsNull,
        anyIsArrayAccess);
  }

  // TODO: will be removed and maybe reason is the references are pointing to the same object
  private static final Predicate<CorelationPair> baseObjectsDifferent =
      cp -> !cp.field1Asgmt().baseObject().equals(cp.field2Asgmt().baseObject());

  private static final Predicate<CorelationPair> anyIsNull = anyIs(ASTNode.NULL_LITERAL);

  private static final Predicate<CorelationPair> anyIsArrayAccess = anyIs(ASTNode.ARRAY_ACCESS);

  private static Predicate<CorelationPair> anyIs(int astCode) {
    return cp -> cp.field1Asgmt().rightSide().getNodeType() == astCode
        || cp.field2Asgmt().rightSide().getNodeType() == astCode;
  }


}

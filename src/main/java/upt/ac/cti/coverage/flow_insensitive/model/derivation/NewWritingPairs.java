package upt.ac.cti.coverage.flow_insensitive.model.derivation;

import java.util.List;
import org.eclipse.jdt.core.IJavaElement;
import org.javatuples.Pair;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;

public final record NewWritingPairs<J extends IJavaElement> (
    List<Pair<Writing<J>, Writing<J>>> writingPairs) implements DerivationResult<J> {


  public static <J extends IJavaElement> NewWritingPairs<J> NULL() {
    return new NewWritingPairs<>(List.of());
  }

  @SafeVarargs
  public static <J extends IJavaElement> NewWritingPairs<J> of(
      Pair<Writing<J>, Writing<J>>... varargs) {
    return new NewWritingPairs<>(List.of(varargs));
  }
}

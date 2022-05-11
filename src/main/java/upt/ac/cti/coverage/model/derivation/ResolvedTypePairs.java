package upt.ac.cti.coverage.model.derivation;

import java.util.List;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;

public final record ResolvedTypePairs<J extends IJavaElement> (List<Pair<IType, IType>> typePairs)
    implements DerivationResult<J> {

  @SafeVarargs
  public static <J extends IJavaElement> ResolvedTypePairs<J> of(
      Pair<IType, IType>... varargs) {
    return new ResolvedTypePairs<>(List.of(varargs));
  }
}

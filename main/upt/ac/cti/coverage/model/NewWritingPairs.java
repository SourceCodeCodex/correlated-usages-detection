package upt.ac.cti.coverage.model;

import java.util.List;
import org.javatuples.Pair;

public final record NewWritingPairs(List<Pair<Writing, Writing>> writingPairs)
    implements DerivationResult {

  @SafeVarargs
  public NewWritingPairs(Pair<Writing, Writing>... varargs) {
    this(List.of(varargs));
  }

  public static final NewWritingPairs NULL = new NewWritingPairs(List.of());
}

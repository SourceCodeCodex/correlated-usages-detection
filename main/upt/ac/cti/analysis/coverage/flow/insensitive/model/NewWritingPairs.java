package upt.ac.cti.analysis.coverage.flow.insensitive.model;

import java.util.List;
import org.javatuples.Pair;

public final record NewWritingPairs(List<Pair<FieldWriting, FieldWriting>> writingPairs)
    implements DerivationResult {

  @SafeVarargs
  public NewWritingPairs(Pair<FieldWriting, FieldWriting>... varargs) {
    this(List.of(varargs));
  }

  public static final NewWritingPairs NULL = new NewWritingPairs(List.of());
}

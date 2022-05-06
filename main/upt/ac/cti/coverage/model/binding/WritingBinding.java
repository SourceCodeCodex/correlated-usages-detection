package upt.ac.cti.coverage.model.binding;

public sealed interface WritingBinding permits Inconclusive, NotLeafBinding, ResolvedBinding {
  public static final Inconclusive INCONCLUSIVE = new Inconclusive();
}

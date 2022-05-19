package upt.ac.cti.coverage.flow_insensitive.model;

import org.eclipse.jdt.core.IJavaElement;

public sealed interface Writing<J extends IJavaElement> permits DerivableWriting<J>, UnderivableWriting<J> {
  public int depth();
}

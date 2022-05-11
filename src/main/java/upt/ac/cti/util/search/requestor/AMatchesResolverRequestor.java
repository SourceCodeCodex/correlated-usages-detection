package upt.ac.cti.util.search.requestor;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.SearchRequestor;

public abstract class AMatchesResolverRequestor<J extends IJavaElement> extends SearchRequestor {
  protected final Set<J> matches = new HashSet<>();

  public Set<J> getMatches() {
    return matches;
  }
}

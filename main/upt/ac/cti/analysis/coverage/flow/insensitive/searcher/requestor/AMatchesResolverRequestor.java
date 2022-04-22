package upt.ac.cti.analysis.coverage.flow.insensitive.searcher.requestor;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.SearchRequestor;

public abstract class AMatchesResolverRequestor extends SearchRequestor {
  protected final Set<IJavaElement> matches = new HashSet<>();

  public Set<IJavaElement> getMatches() {
    return matches;
  }
}

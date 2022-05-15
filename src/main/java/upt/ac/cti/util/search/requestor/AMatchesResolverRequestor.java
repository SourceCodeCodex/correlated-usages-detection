package upt.ac.cti.util.search.requestor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.SearchRequestor;

public abstract class AMatchesResolverRequestor<J extends IJavaElement> extends SearchRequestor {
  protected final List<J> matches = new ArrayList<>();

  public List<J> getMatches() {
    return matches;
  }
}

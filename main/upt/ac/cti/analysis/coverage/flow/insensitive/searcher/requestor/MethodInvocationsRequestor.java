package upt.ac.cti.analysis.coverage.flow.insensitive.searcher.requestor;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.search.MethodReferenceMatch;
import org.eclipse.jdt.core.search.SearchMatch;

public class MethodInvocationsRequestor extends AMatchesResolverRequestor {
  @Override
  public void acceptSearchMatch(SearchMatch match) {
    if (match instanceof MethodReferenceMatch ref) {
      var javaElement = (IMember) match.getElement();
      if (!matches.contains(javaElement)) {
        matches.add(javaElement);
        return;
      }
    }
  }
}

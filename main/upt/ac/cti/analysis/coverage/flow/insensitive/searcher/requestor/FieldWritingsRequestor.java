package upt.ac.cti.analysis.coverage.flow.insensitive.searcher.requestor;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.FieldReferenceMatch;
import org.eclipse.jdt.core.search.SearchMatch;

public class FieldWritingsRequestor extends AMatchesResolverRequestor {

  @Override
  public void acceptSearchMatch(SearchMatch match) {
    if (match instanceof FieldReferenceMatch ref && ref.isWriteAccess()) {
      var method = (IMethod) match.getElement();
      matches.add(method);
    }
  }

}

package upt.ac.cti.util.search.requestor;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.FieldReferenceMatch;
import org.eclipse.jdt.core.search.SearchMatch;

public class WritingsRequestor extends AMatchesResolverRequestor<IMethod> {

  @Override
  public void acceptSearchMatch(SearchMatch match) {
    if (match instanceof FieldReferenceMatch ref && ref.isWriteAccess()) {
      var method = (IMethod) match.getElement();
      matches.add(method);
    }
  }

}

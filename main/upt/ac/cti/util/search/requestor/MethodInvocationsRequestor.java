package upt.ac.cti.util.search.requestor;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.MethodReferenceMatch;
import org.eclipse.jdt.core.search.SearchMatch;

public class MethodInvocationsRequestor extends AMatchesResolverRequestor<IMember> {
  @Override
  public void acceptSearchMatch(SearchMatch match) {
    if (match instanceof MethodReferenceMatch ref && match.getElement() instanceof IMethod) {
      var javaElement = (IMember) match.getElement();
      if (!matches.contains(javaElement)) {
        matches.add(javaElement);
        return;
      }
    }
  }
}

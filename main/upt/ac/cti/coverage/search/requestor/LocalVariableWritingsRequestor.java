package upt.ac.cti.coverage.search.requestor;

import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.LocalVariableDeclarationMatch;
import org.eclipse.jdt.core.search.LocalVariableReferenceMatch;
import org.eclipse.jdt.core.search.SearchMatch;

public class LocalVariableWritingsRequestor extends AMatchesResolverRequestor {
  @Override
  public void acceptSearchMatch(SearchMatch match) {
    if (match instanceof LocalVariableReferenceMatch ref && ref.isWriteAccess()) {
      var method = (IMethod) ref.getElement();
      matches.add(method);
    }

    if (match instanceof LocalVariableDeclarationMatch dec) {
      var method = (IMethod) ((ILocalVariable) dec.getElement()).getDeclaringMember();
      matches.add(method);
    }
  }

}

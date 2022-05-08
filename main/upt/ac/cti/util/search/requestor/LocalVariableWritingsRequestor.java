package upt.ac.cti.util.search.requestor;

import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.LocalVariableDeclarationMatch;
import org.eclipse.jdt.core.search.LocalVariableReferenceMatch;
import org.eclipse.jdt.core.search.SearchMatch;

public class LocalVariableWritingsRequestor extends AMatchesResolverRequestor<IMethod> {
  @Override
  public void acceptSearchMatch(SearchMatch match) {
    if (match instanceof LocalVariableReferenceMatch ref && ref.isWriteAccess()
        && ref.getElement() instanceof IMethod) {
      var method = (IMethod) ref.getElement();
      matches.add(method);
    }

    if (match instanceof LocalVariableDeclarationMatch dec
        && dec.getElement() instanceof ILocalVariable) {
      var method = (IMethod) ((ILocalVariable) dec.getElement()).getDeclaringMember();
      matches.add(method);
    }
  }

}

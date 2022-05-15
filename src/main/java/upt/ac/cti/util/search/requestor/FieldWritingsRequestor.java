package upt.ac.cti.util.search.requestor;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.FieldReferenceMatch;
import org.eclipse.jdt.core.search.SearchMatch;

public class FieldWritingsRequestor extends AMatchesResolverRequestor<IMethod> {

  @Override
  public void acceptSearchMatch(SearchMatch match) {
    if (match instanceof FieldReferenceMatch ref && ref.isWriteAccess()
        && match.getElement() instanceof IMethod) {
      var method = (IMethod) match.getElement();

      if (method.isLambdaMethod()) {
        var j = (IJavaElement) method;
        while (!(j.getParent() instanceof IType) || ((IType) j.getParent()).isLambda()) {
          j = j.getParent();
        }

        if (j instanceof IMethod) {
          matches.add((IMethod) j);
        }

      } else {
        matches.add(method);
      }
    }
  }

}

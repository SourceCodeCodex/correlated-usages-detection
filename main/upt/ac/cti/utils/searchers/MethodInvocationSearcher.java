package upt.ac.cti.utils.searchers;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.MethodReferenceMatch;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

public class MethodInvocationSearcher {

  private static MethodInvocationSearcher instance = new MethodInvocationSearcher();

  private MethodInvocationSearcher() {

  }

  public static MethodInvocationSearcher instance() {
    return instance;
  }

  public List<IMember> searchFieldWrites(IMethod iMethod) {
    var matches = new ArrayList<IMember>();

    var scope = SearchEngine.createJavaSearchScope(new IJavaElement[] {iMethod.getJavaProject()});

    var searchParttern = SearchPattern.createPattern(iMethod, IJavaSearchConstants.REFERENCES,
        SearchPattern.R_EXACT_MATCH);

    SearchRequestor requestor = new SearchRequestor() {
      @Override
      public void acceptSearchMatch(SearchMatch match) {
        if (match instanceof MethodReferenceMatch ref) {
          var iJavaElement = (IMember) match.getElement();
          if (!matches.contains(iJavaElement)) {
            matches.add(iJavaElement);
            return;
          }
        }
      }
    };

    var searchEngine = new SearchEngine();

    try {
      searchEngine.search(searchParttern,
          new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()},
          scope, requestor, new NullProgressMonitor());
    } catch (CoreException e) {
      e.printStackTrace();
    }

    return matches;
  }

}

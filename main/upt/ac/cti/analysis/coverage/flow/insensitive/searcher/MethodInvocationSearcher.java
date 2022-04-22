package upt.ac.cti.analysis.coverage.flow.insensitive.searcher;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
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

  private static final Logger logger = Logger.getLogger(MethodInvocationSearcher.class.getName());

  private MethodInvocationSearcher() {

  }

  public static MethodInvocationSearcher instance() {
    return instance;
  }

  public List<IMember> searchFieldWrites(IMethod method) {
    var matches = new ArrayList<IMember>();

    var scope = SearchEngine.createJavaSearchScope(new IJavaElement[] {method.getJavaProject()});

    var searchParttern = SearchPattern.createPattern(method, IJavaSearchConstants.REFERENCES,
        SearchPattern.R_EXACT_MATCH);

    SearchRequestor requestor = new SearchRequestor() {
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
    };

    var searchEngine = new SearchEngine();

    try {
      searchEngine.search(searchParttern,
          new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()},
          scope, requestor, new NullProgressMonitor());
    } catch (CoreException e) {
      var ste = e.getStackTrace()[0];
      logger.throwing(ste.getClassName(), ste.getMethodName(), e);
    }

    return matches;
  }

}

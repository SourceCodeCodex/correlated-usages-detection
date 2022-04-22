package upt.ac.cti.analysis.coverage.flow.insensitive.searcher;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.FieldReferenceMatch;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.LocalVariableDeclarationMatch;
import org.eclipse.jdt.core.search.LocalVariableReferenceMatch;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

public class WritingsSearcher {
  private static WritingsSearcher instance = new WritingsSearcher();

  private static final Logger logger = Logger.getLogger(WritingsSearcher.class.getName());

  private WritingsSearcher() {

  }

  public static WritingsSearcher instance() {
    return instance;
  }

  public Set<IMethod> searchFieldWrites(IField field) {
    var matches = new HashSet<IMethod>();

    var scope = SearchEngine.createJavaSearchScope(new IJavaElement[] {field.getJavaProject()});

    var searchParttern = SearchPattern.createPattern(field, IJavaSearchConstants.REFERENCES,
        SearchPattern.R_EXACT_MATCH);

    SearchRequestor requestor = new SearchRequestor() {
      @Override
      public void acceptSearchMatch(SearchMatch match) {
        if (match instanceof FieldReferenceMatch ref && ref.isWriteAccess()) {
          var method = (IMethod) match.getElement();
          matches.add(method);
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

  public Set<IMethod> searchLocalVariablesWrites(ILocalVariable localVariable) {
    var matches = new HashSet<IMethod>();

    var scope =
        SearchEngine.createJavaSearchScope(new IJavaElement[] {localVariable.getJavaProject()});

    var searchParttern =
        SearchPattern.createPattern(localVariable, IJavaSearchConstants.ALL_OCCURRENCES,
            SearchPattern.R_EXACT_MATCH);

    SearchRequestor requestor = new SearchRequestor() {
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

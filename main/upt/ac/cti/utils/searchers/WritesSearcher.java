package upt.ac.cti.utils.searchers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.FieldReferenceMatch;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.LocalVariableReferenceMatch;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

public class WritesSearcher {
  private static WritesSearcher instance = new WritesSearcher();

  public static WritesSearcher instance() {
    return instance;
  }

  public List<IMethod> searchFieldWrites(IField iField) {
    var matches = new ArrayList<IMethod>();

    var scope = SearchEngine.createJavaSearchScope(new IJavaElement[] {iField.getJavaProject()});

    var searchParttern = SearchPattern.createPattern(iField, IJavaSearchConstants.REFERENCES,
        SearchPattern.R_EXACT_MATCH);

    SearchRequestor requestor = new SearchRequestor() {
      @Override
      public void acceptSearchMatch(SearchMatch match) {
        if (match instanceof FieldReferenceMatch fieldRef) {
          if (fieldRef.isWriteAccess()) {
            var iMethod = (IMethod) match.getElement();
            if (!matches.contains(iMethod)) {
              matches.add(iMethod);
            }
          }
        } else {
          Logger.getGlobal().warning("Execution should never reach this code!");
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

  public List<IMethod> searchLocalVariablesWrites(ILocalVariable iLocalVariable) {
    var matches = new ArrayList<IMethod>();

    var scope =
        SearchEngine.createJavaSearchScope(new IJavaElement[] {iLocalVariable.getJavaProject()});

    var searchParttern =
        SearchPattern.createPattern(iLocalVariable, IJavaSearchConstants.ALL_OCCURRENCES,
            SearchPattern.R_EXACT_MATCH);

    SearchRequestor requestor = new SearchRequestor() {
      @Override
      public void acceptSearchMatch(SearchMatch match) {
        if(match instanceof LocalVariableReferenceMatch localVarRef) {

        }
        System.out.println(match.getElement());
        System.out.println(match);
        System.out.println(match.getClass().getCanonicalName());
        System.out.println("----------");
        // if (((LocalVariableReferenceMatch) match).isWriteAccess()) {
         var iMethod = (IMethod) match.getElement();
         if (!matches.contains(iMethod)) {
         matches.add(iMethod);
        // }
        // }
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

package upt.ac.cti.util.search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.cache.CacheRegion;
import upt.ac.cti.util.search.requestor.AMatchesResolverRequestor;
import upt.ac.cti.util.search.requestor.LocalVariableWritingsRequestor;
import upt.ac.cti.util.search.requestor.MethodInvocationsRequestor;
import upt.ac.cti.util.search.requestor.ReferencesRequestor;
import upt.ac.cti.util.search.requestor.FieldWritingsRequestor;

public final class JavaEntitySearcher {

  private final Cache<IJavaElement, List<IJavaElement>> cache =
      new Cache<>(CacheRegion.SEARCH);

  public Set<IMethod> searchFieldWritings(IField field) {
    var requestor = new FieldWritingsRequestor();

    var searchParttern = SearchPattern.createPattern(field,
        IJavaSearchConstants.WRITE_ACCESSES,
        SearchPattern.R_EXACT_MATCH);

    return searchJavaElement(field, requestor, searchParttern);
  }

  public Set<IMethod> searchFieldReferences(IField field) {
    var requestor = new ReferencesRequestor();

    var searchParttern = SearchPattern.createPattern(field,
        IJavaSearchConstants.REFERENCES,
        SearchPattern.R_EXACT_MATCH);

    return searchJavaElement(field, requestor, searchParttern);
  }

  public Set<IMethod> searchLocalVariableWritings(ILocalVariable localVariable) {
    var requestor = new LocalVariableWritingsRequestor();

    var searchParttern =
        SearchPattern.createPattern(localVariable,
            IJavaSearchConstants.ALL_OCCURRENCES,
            SearchPattern.R_EXACT_MATCH);

    return searchJavaElement(localVariable, requestor, searchParttern);
  }

  public Set<IMember> searchMethodInvocations(IMethod method) {
    var requestor = new MethodInvocationsRequestor();

    var searchParttern = SearchPattern.createPattern(method,
        IJavaSearchConstants.REFERENCES,
        SearchPattern.R_EXACT_MATCH);

    return searchJavaElement(method, requestor, searchParttern);

  }

  @SuppressWarnings("unchecked")
  private <T extends IJavaElement, S extends IJavaElement> Set<T> searchJavaElement(S element,
      AMatchesResolverRequestor<T> requestor, SearchPattern pattern) {
    var cachedMatches = cache.get(element);
    if (cachedMatches.isPresent()) {
      return cachedMatches
          .get()
          .stream()
          .map(member -> (T) member)
          .collect(Collectors.toSet());
    }


    List<T> result = useSearchEngine(requestor, pattern, element.getJavaProject());

    cache.put((IJavaElement) element, (List<IJavaElement>) result);

    return new HashSet<>(result);
  }

  private <T extends IJavaElement> List<T> useSearchEngine(
      AMatchesResolverRequestor<T> requestor,
      SearchPattern searchParttern,
      IJavaProject project) {
    try {
      new SearchEngine().search(searchParttern,
          new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()},
          (project == null) ? SearchEngine.createWorkspaceScope()
              : SearchEngine.createJavaSearchScope(new IJavaElement[] {project}),
          requestor, new NullProgressMonitor());
      return requestor.getMatches();
    } catch (CoreException e) {
      e.printStackTrace();
    }
    return List.of();
  }

}

package upt.ac.cti.analysis.coverage.flow.insensitive.searcher;

import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.requestor.AMatchesResolverRequestor;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.requestor.FieldWritingsRequestor;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.requestor.LocalVariableWritingsRequestor;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.requestor.MethodInvocationsRequestor;
import upt.ac.cti.util.cache.Cache;

public final class JavaEntitySearcher {

  private final SearchEngine searchEngine = new SearchEngine();

  private final Cache<IJavaElement, Set<IJavaElement>> cache = new Cache<>();

  private IJavaSearchScope createScope(IJavaElement javaElement) {
    return SearchEngine.createJavaSearchScope(new IJavaElement[] {javaElement.getJavaProject()});
  }

  private Set<IJavaElement> search(IJavaSearchScope scope, AMatchesResolverRequestor requestor,
      SearchPattern searchParttern) {
    try {
      searchEngine.search(searchParttern,
          new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()},
          scope, requestor, new NullProgressMonitor());
      return requestor.getMatches();
    } catch (CoreException e) {
      e.printStackTrace();
    }
    return Set.of();
  }

  public Set<IMethod> searchFieldWritings(IField field) {
    var cachedMatches = cache.get(field);
    if (cachedMatches.isPresent()) {
      return cachedMatches
          .get()
          .stream()
          .map(method -> (IMethod) method)
          .collect(Collectors.toSet());
    }

    var scope = createScope(field);
    var requestor = new FieldWritingsRequestor();

    var searchParttern = SearchPattern.createPattern(field,
        IJavaSearchConstants.REFERENCES,
        SearchPattern.R_EXACT_MATCH);

    var result = search(scope, requestor, searchParttern);

    cache.put(field, result);

    return result
        .stream()
        .map(method -> (IMethod) method)
        .collect(Collectors.toSet());
  }

  public Set<IMethod> searchLocalVariableWritings(ILocalVariable localVariable) {
    var cachedMatches = cache.get(localVariable);
    if (cachedMatches.isPresent()) {
      return cachedMatches
          .get()
          .stream()
          .map(method -> (IMethod) method)
          .collect(Collectors.toSet());
    }

    var scope = createScope(localVariable);
    var requestor = new LocalVariableWritingsRequestor();

    var searchParttern =
        SearchPattern.createPattern(localVariable,
            IJavaSearchConstants.ALL_OCCURRENCES,
            SearchPattern.R_EXACT_MATCH);

    var result = search(scope, requestor, searchParttern);

    cache.put(localVariable, result);

    return result
        .stream()
        .map(method -> (IMethod) method)
        .collect(Collectors.toSet());
  }

  public Set<IMember> searchMethodInvocations(IMethod method) {
    var cachedMatches = cache.get(method);
    if (cachedMatches.isPresent()) {
      return cachedMatches
          .get()
          .stream()
          .map(member -> (IMember) member)
          .collect(Collectors.toSet());
    }

    var scope = createScope(method);
    var requestor = new MethodInvocationsRequestor();

    var searchParttern = SearchPattern.createPattern(method,
        IJavaSearchConstants.REFERENCES,
        SearchPattern.R_EXACT_MATCH);

    var result = search(scope, requestor, searchParttern);

    cache.put(method, result);

    return result
        .stream()
        .map(member -> (IMember) member)
        .collect(Collectors.toSet());
  }

}

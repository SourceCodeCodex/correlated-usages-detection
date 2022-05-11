package upt.ac.cti.util.search;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
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
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.cache.CacheRegions;
import upt.ac.cti.util.logging.RLogger;
import upt.ac.cti.util.search.requestor.AMatchesResolverRequestor;
import upt.ac.cti.util.search.requestor.LocalVariableWritingsRequestor;
import upt.ac.cti.util.search.requestor.MethodInvocationsRequestor;
import upt.ac.cti.util.search.requestor.ReferencesRequestor;
import upt.ac.cti.util.search.requestor.WritingsRequestor;

public final class JavaEntitySearcher {

  private static final Logger logger = RLogger.get();

  private final Cache<IJavaElement, Set<IJavaElement>> cache = new Cache<>(CacheRegions.SEARCH);

  public Set<IMethod> searchFieldWritings(IField field) {
    var requestor = new WritingsRequestor();

    var searchParttern = SearchPattern.createPattern(field,
        IJavaSearchConstants.REFERENCES,
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

    var scopeOpt = createScope(element);
    if (scopeOpt.isEmpty()) {
      warnCouldNotSearchProjectNull(element);
      return new HashSet<>();
    }

    var scope = scopeOpt.get();

    Set<T> result = useSearchEngine(scope, requestor, pattern);

    cache.put((IJavaElement) element, (Set<IJavaElement>) result);

    return result;
  }

  private <T extends IJavaElement> Set<T> useSearchEngine(IJavaSearchScope scope,
      AMatchesResolverRequestor<T> requestor,
      SearchPattern searchParttern) {
    try {
      new SearchEngine().search(searchParttern,
          new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()},
          scope, requestor, new NullProgressMonitor());
      return requestor.getMatches();
    } catch (CoreException e) {
      e.printStackTrace();
    }
    return Set.of();
  }

  private Optional<IJavaSearchScope> createScope(IJavaElement javaElement) {
    if (javaElement.getJavaProject() == null) {
      return Optional.empty();
    }
    return Optional
        .of(SearchEngine.createJavaSearchScope(new IJavaElement[] {javaElement.getJavaProject()}));
  }

  private void warnCouldNotSearchProjectNull(IJavaElement element) {
    var log = String.format("Could not parse: %s. Type: %d. Reason: %s",
        element.getElementName(), element.getElementType(), "Java Project is null.");
    logger.warning(log);
  }
}

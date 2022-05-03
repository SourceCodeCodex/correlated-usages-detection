package upt.ac.cti.coverage.search;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import upt.ac.cti.coverage.search.requestor.AMatchesResolverRequestor;
import upt.ac.cti.coverage.search.requestor.LocalVariableWritingsRequestor;
import upt.ac.cti.coverage.search.requestor.MethodInvocationsRequestor;
import upt.ac.cti.coverage.search.requestor.ReferencesRequestor;
import upt.ac.cti.coverage.search.requestor.WritingsRequestor;
import upt.ac.cti.util.cache.Cache;

public final class JavaEntitySearcher {

  private static final Logger logger = Logger.getLogger(JavaEntitySearcher.class.getName());

  private final SearchEngine searchEngine = new SearchEngine();

  private final Cache<IJavaElement, Set<IJavaElement>> cache = new Cache<>();

  private Optional<IJavaSearchScope> createScope(IJavaElement javaElement) {
    if (javaElement.getJavaProject() == null) {
      return Optional.empty();
    }
    return Optional
        .of(SearchEngine.createJavaSearchScope(new IJavaElement[] {javaElement.getJavaProject()}));
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

  public Set<IMethod> searchWritings(IJavaElement javaElement) {
    var cachedMatches = cache.get(javaElement);
    if (cachedMatches.isPresent()) {
      return cachedMatches
          .get()
          .stream()
          .map(method -> (IMethod) method)
          .collect(Collectors.toSet());
    }

    var scopeOpt = createScope(javaElement);
    if (scopeOpt.isEmpty()) {
      warnCouldNotSearchProjectNull(javaElement);
      return new HashSet<>();
    }

    var scope = scopeOpt.get();
    var requestor = new WritingsRequestor();

    var searchParttern = SearchPattern.createPattern(javaElement,
        IJavaSearchConstants.REFERENCES,
        SearchPattern.R_EXACT_MATCH);

    var result = search(scope, requestor, searchParttern);

    cache.put(javaElement, result);

    return result
        .stream()
        .map(method -> (IMethod) method)
        .collect(Collectors.toSet());
  }

  public Set<IMethod> searchReferences(IJavaElement javaElement) {
    var cachedMatches = cache.get(javaElement);
    if (cachedMatches.isPresent()) {
      return cachedMatches
          .get()
          .stream()
          .map(method -> (IMethod) method)
          .collect(Collectors.toSet());
    }

    var scopeOpt = createScope(javaElement);
    if (scopeOpt.isEmpty()) {
      warnCouldNotSearchProjectNull(javaElement);
      return new HashSet<>();
    }

    var scope = scopeOpt.get();
    var requestor = new ReferencesRequestor();

    var searchParttern = SearchPattern.createPattern(javaElement,
        IJavaSearchConstants.REFERENCES,
        SearchPattern.R_EXACT_MATCH);

    var result = search(scope, requestor, searchParttern);

    cache.put(javaElement, result);

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

    if (localVariable.getJavaProject() == null) {
      return new HashSet<>();
    }

    var scopeOpt = createScope(localVariable);
    if (scopeOpt.isEmpty()) {
      warnCouldNotSearchProjectNull(localVariable);
      return new HashSet<>();
    }

    var scope = scopeOpt.get();
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

    var scopeOpt = createScope(method);
    if (scopeOpt.isEmpty()) {
      warnCouldNotSearchProjectNull(method);
      return new HashSet<>();
    }

    var scope = scopeOpt.get();
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

  private void warnCouldNotSearchProjectNull(IJavaElement element) {
    var log = String.format("Could not parse: %s. Type: %d. Reason: %s",
        element.getElementName(), element.getElementType(), "Java Project is null.");
    logger.warning(log);
  }
}

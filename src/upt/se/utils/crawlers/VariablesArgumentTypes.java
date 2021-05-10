package upt.se.utils.crawlers;

import static upt.se.utils.helpers.Equals.isEqual;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.ArrayList;
import java.util.logging.Level;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.core.search.TypeReferenceMatch;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import thesis.metamodel.entity.MParameter;
import upt.se.utils.visitors.VariableBindingVisitor;

public class VariablesArgumentTypes {

  public static List<ITypeBinding> getUsages(MParameter entity) {
    return Try.of(() -> entity.getUnderlyingObject().getDeclaringClass())
        .map(type -> Tuple.of(getParameterNumber(entity), findVariablesArguments(type)))
        .map(pair -> pair.apply((argumentPos, variables) -> variables.map(argument -> argument.get(argumentPos))))
        .onFailure(
            t -> LOGGER.log(Level.SEVERE, "Could not find parameter in class declaration", t))
        .orElse(() -> Try.success(List.empty()))
        .get();
  }

  private static int getParameterNumber(MParameter entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .map(type -> Tuple.of(type, List.of(type.getDeclaringClass().getTypeParameters())))
        .map(tuple -> tuple._2.zipWithIndex()
            .find(argument -> isEqual(argument._1, tuple._1))
            .map(argument -> argument._2))
        .flatMap(Option::toTry)
        .orElse(() -> Try.success(0))
        .get();
  }

  private static final List<List<ITypeBinding>> findVariablesArguments(ITypeBinding type) {
    java.util.List<java.util.List<ITypeBinding>> types = new ArrayList<>();

    SearchPattern pattern = SearchPattern.createPattern(type.getJavaElement(),
        IJavaSearchConstants.FIELD_DECLARATION_TYPE_REFERENCE
            | IJavaSearchConstants.LOCAL_VARIABLE_DECLARATION_TYPE_REFERENCE
            | IJavaSearchConstants.CLASS_INSTANCE_CREATION_TYPE_REFERENCE
            | IJavaSearchConstants.PARAMETER_DECLARATION_TYPE_REFERENCE);

    IJavaSearchScope scope = SearchEngine.createWorkspaceScope();

    SearchRequestor requestor = new SearchRequestor() {
      public void acceptSearchMatch(SearchMatch match) {
        Try.of(() -> ((TypeReferenceMatch) match))
            .map(TypeReferenceMatch::getElement)
            .filter(element -> element instanceof IMember)
            .map(element -> ((IMember) element).getCompilationUnit())
            .map(compilationUnit -> VariableBindingVisitor.convert(compilationUnit, type))
            .map(variables -> List.ofAll(variables)
                .map(variable -> variable.getType())
                .map(type -> List.of(type.getTypeArguments()).asJava()).asJava())
            .onSuccess(list -> types.addAll(list));
      }
    };

    SearchEngine searchEngine = new SearchEngine();

    try {
      searchEngine.search(pattern,
          new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()},
          scope, requestor, new NullProgressMonitor());
    } catch (CoreException e) {
      LOGGER.log(Level.SEVERE, "An error has occurred while searching", e);
    }

    return List.ofAll(types).map(List::ofAll);
  }

}

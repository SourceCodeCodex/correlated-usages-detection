package upt.se.utils.store;

import static upt.se.utils.builders.ListBuilder.toList;
import static upt.se.utils.helpers.ClassNames.*;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import static upt.se.utils.store.ITypeStore.convert;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import io.vavr.control.Option;
import io.vavr.control.Try;
import thesis.metamodel.entity.MArgumentType;
import upt.se.utils.builders.ListBuilder;
import upt.se.utils.visitors.VariableBindingVisitor;

public class InterfaceBindingStore {

  public static List<ITypeBinding> getAllSubtypes(ITypeBinding typeBinding) {
    return Try.of(() -> convert(typeBinding))
        .flatMap(maybeType -> Option.ofOptional(maybeType).toTry())
        .mapTry(type -> Tuple.of(type, type.newTypeHierarchy(new NullProgressMonitor())))
        .map(tuple -> tuple._2.getAllSubtypes(tuple._1))
        .map(types -> io.vavr.collection.List.of(types))
        .flatMap(list -> Try.of(() -> list.map(type -> convert(type))
            .map(Option::ofOptional)
            .map(Option::toTry)
            .map(Try::get)))
        .map(list -> list.toJavaList())
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(Collections.emptyList()))
        .get();
  }

  public static List<ITypeBinding> getTypeArguments(List<ITypeBinding> declaringClasses,
      List<ITypeBinding> allSubtypes) {
    final io.vavr.collection.List<ITypeBinding> declaring = toList(declaringClasses).map(type -> type.getSuperclass())
                                                                                    .filter(type -> type.getTypeArguments().length > 0)
                                                                                    .flatMap(type -> toList(type.getTypeArguments()))
                                                                                    .distinctBy((p1, p2) -> isEqual(p1, p2) ? 0 : 1);
    final io.vavr.collection.List<ITypeBinding> subtypes = toList(allSubtypes);

    return subtypes.filter(subType -> declaring.exists(parameter -> isEqual(parameter, subType))).asJava();
  }

  public static List<ITypeBinding> usagesInDeclaringClass(MArgumentType entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .map(type -> Tuple.of(type, toList(getAllSubtypes(type.getDeclaringClass()))))
        .map(tuple -> tuple.map(type -> toList(type.getDeclaringClass().getTypeParameters())
                                              .zipWithIndex()
                                              .filter(parameter -> isEqual(type, parameter._1))
                                              .head(),
                                types -> types.map(type -> type.getSuperclass().getTypeArguments())
                                              .map(ListBuilder::toList)
                                              .map(list -> list.zipWithIndex())))
        .map(tuple -> tuple._2.map(arguments -> arguments.filter(argument -> argument._2 == tuple._1._2)
                                                         .head()))
        .map(arguments -> arguments.map(argument -> argument._1))
        .map(arguments -> arguments.distinctBy((p1, p2) -> isEqual(p1, p2) ?  0 : 1))
        .map(arguments -> arguments.asJava())
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(Collections.emptyList()))
        .get();
  }

  public static List<ITypeBinding> usagesInInheritance(MArgumentType entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .map(type -> Tuple.of(type.getDeclaringClass(), type.getSuperclass()))
        .map(tuple -> Tuple.of(getAllSubtypes(tuple._1), getAllSubtypes(tuple._2)))
        .map(tuple -> tuple.map2(allSubtypes -> toList(allSubtypes).prepend(entity.getUnderlyingObject().getSuperclass()).asJava()))
        .map(tuple -> getTypeArguments(tuple._1, tuple._2))
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(Collections.emptyList()))
        .get();
  }


  public static List<ITypeBinding> usagesInVariables(MArgumentType entity) {
    return Try.of(() -> entity.getUnderlyingObject().getDeclaringClass())
        .map(type -> toList(findVariablesArguments(type)))
        .map(variables -> variables.map(arguments -> arguments.get(getParameterNumber(entity))))
        .map(list -> list.asJava())
        .onFailure(t -> LOGGER.log(Level.SEVERE, "Could not find parameter in class declaration", t))
        .orElse(() -> Try.success(Collections.emptyList()))
        .get();
  }

  private static int getParameterNumber(MArgumentType entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .map(type -> Tuple.of(type, toList(type.getDeclaringClass().getTypeArguments())))
        .map(tuple -> tuple._2.zipWithIndex()
                              .find(argument -> isEqual(argument._1, tuple._1))
                              .map(argument -> argument._2))
        .flatMap(Option::toTry)
        .orElse(() -> Try.success(0))
        .get();
  }

  public static final List<List<ITypeBinding>> findVariablesArguments(ITypeBinding type) {
    List<List<ITypeBinding>> types = new ArrayList<>();

    SearchPattern pattern = SearchPattern.createPattern(type.getJavaElement(),
        IJavaSearchConstants.FIELD_DECLARATION_TYPE_REFERENCE
            | IJavaSearchConstants.LOCAL_VARIABLE_DECLARATION_TYPE_REFERENCE
            | IJavaSearchConstants.CLASS_INSTANCE_CREATION_TYPE_REFERENCE);

    IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
    
    SearchRequestor requestor = new SearchRequestor() {
      public void acceptSearchMatch(SearchMatch match) {
        Try.of(() -> ((TypeReferenceMatch) match))
            .map(TypeReferenceMatch::getElement)
            .filter(element -> element instanceof IMember)
            .map(element -> ((IMember) element).getCompilationUnit())
            .map(compilationUnit -> VariableBindingVisitor.convert(compilationUnit))
            .map(variables -> toList(variables).map(variable -> variable.getType())
                                                .map(type -> toList(type.getTypeArguments()))
                                                .map(arguments -> arguments.asJava()))
            .map(list -> list.asJava())
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

    return types;
  }

}

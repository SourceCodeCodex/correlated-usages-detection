package upt.se.utils.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.core.search.TypeReferenceMatch;
import thesis.metamodel.entity.MTypeParameter;
import upt.se.utils.Pair;
import upt.se.utils.visitors.VariableBindingVisitor;
import upt.se.utils.visitors.GenericParameterBindingVisitor;
import upt.se.utils.visitors.HierarchyBindingVisitor;

public final class ITypeStore {
  private static Map<IJavaProject, List<IType>> allTypes = new HashMap<>();
  private static Map<IType, Optional<ITypeBinding>> typeCache = new HashMap<>();
  private static Map<ITypeBinding, Optional<IType>> typeBindingCache = new HashMap<>();

  public static final List<IType> getAllTypes(IJavaProject javaproject) {
    if (allTypes.containsKey(javaproject)) {
      return allTypes.get(javaproject);
    }

    List<IType> typeList = new ArrayList<IType>();
    try {
      IPackageFragmentRoot[] roots = javaproject.getPackageFragmentRoots();
      for (int i = 0; i < roots.length; i++) {
        IPackageFragmentRoot root = roots[i];
        if (root.getKind() == IPackageFragmentRoot.K_SOURCE) {
          IJavaElement[] javaElements = root.getChildren();
          for (int j = 0; j < javaElements.length; j++) {
            IJavaElement javaElement = javaElements[j];
            if (javaElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
              IPackageFragment pf = (IPackageFragment) javaElement;
              ICompilationUnit[] compilationUnits = pf.getCompilationUnits();
              for (int k = 0; k < compilationUnits.length; k++) {
                ICompilationUnit unit = compilationUnits[k];
                if (unit.isStructureKnown()) {
                  typeList.addAll(Arrays.asList(unit.getTypes()));
                }
              }
            }
          }
        }
      }
    } catch (CoreException e) {
      e.printStackTrace();
    }

    allTypes.put(javaproject, typeList);
    return typeList;
  }

  public static final Optional<IType> convert(ITypeBinding typeBinding) {
    if (typeBindingCache.containsKey(typeBinding)
        && typeBindingCache.get(typeBinding).isPresent()) {
      return typeBindingCache.get(typeBinding);
    }

    typeBindingCache.put(typeBinding,
        getAllTypes(typeBinding.getSuperclass().getJavaElement().getJavaProject()).stream()
            .filter(t -> t.getFullyQualifiedName()
                .equals(typeBinding.isParameterizedType() ? typeBinding.getBinaryName()
                    : typeBinding.getQualifiedName()))
            .findFirst());

    Optional<IType> result = typeBindingCache.get(typeBinding);
    result.ifPresent(t -> typeCache.put(t, Optional.ofNullable(typeBinding)));

    return result;
  }

  public static final Optional<ITypeBinding> convert(IType type) {
    if (typeCache.containsKey(type) && typeCache.get(type).isPresent()) {
      return typeCache.get(type);
    }
    typeCache.put(type, GenericParameterBindingVisitor.convert(type.getCompilationUnit()).stream()
        .filter(t -> t.getQualifiedName().equals(type.getFullyQualifiedName())).findFirst());
    typeCache.put(type, HierarchyBindingVisitor.convert(type.getCompilationUnit()).stream()
        .filter(t -> t.getQualifiedName().equals(type.getFullyQualifiedName())).findFirst());

    Optional<ITypeBinding> result = typeCache.get(type);
    result.ifPresent(t -> typeBindingCache.put(t, Optional.ofNullable(type)));

    return result;
  }

  public static final Optional<List<ITypeBinding>> convert(List<IType> types) {
    List<Optional<ITypeBinding>> result =
        types.stream().map(t -> convert(t)).collect(Collectors.toList());

    if (result.stream().anyMatch(o -> !o.isPresent())) {
      return Optional.empty();
    }
    return Optional.of(result.stream().map(o -> o.get()).collect(Collectors.toList()));
  }

  public static final List<Pair<IType, List<ITypeBinding>>> getAllChildrenTypes(IType type) {
    try {
      ITypeHierarchy hierarchy = type.newTypeHierarchy(new NullProgressMonitor());

      List<IType> allSubtypes = Arrays.asList(hierarchy.getAllSubtypes(type));

      return allSubtypes.stream()
          .map(t -> HierarchyBindingVisitor.convert(t.getCompilationUnit()).stream()
              .filter(t1 -> t1.getQualifiedName().equals(t.getFullyQualifiedName())).findFirst()
              .map(t1 -> new Pair<>(t, Arrays.asList(t1.getSuperclass().getTypeArguments()))).get())
          .filter(p -> p.getSecond().size() > 0).collect(Collectors.toList());

    } catch (JavaModelException e) {
      e.printStackTrace();
    }
    return Collections.emptyList();
  }

  public static final List<Pair<IVariableBinding, List<ITypeBinding>>> findAttributeUsages(
      IType type) {
    final List<Pair<IVariableBinding, List<ITypeBinding>>> attributes = new ArrayList<>();
    final List<ICompilationUnit> cache = new ArrayList<>();

    SearchPattern pattern = SearchPattern.createPattern(type,
        IJavaSearchConstants.FIELD_DECLARATION_TYPE_REFERENCE
            | IJavaSearchConstants.LOCAL_VARIABLE_DECLARATION_TYPE_REFERENCE
            | IJavaSearchConstants.CLASS_INSTANCE_CREATION_TYPE_REFERENCE);

    IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
    SearchRequestor requestor = new SearchRequestor() {
      public void acceptSearchMatch(SearchMatch match) {

        TypeReferenceMatch typeMatch = (TypeReferenceMatch) match;
        Optional<ICompilationUnit> maybeCompilationUnit = Optional.empty();
        if (typeMatch.getElement() instanceof IMember) {
          maybeCompilationUnit =
              Optional.of(((IMember) typeMatch.getElement()).getCompilationUnit());
        }

        maybeCompilationUnit.filter(compilationUnit -> !cache.contains(compilationUnit))
            .ifPresent(compilationUnit -> {
              cache.add(compilationUnit);

              HashSet<IVariableBinding> variables =
                  VariableBindingVisitor.convert(compilationUnit);

              variables.forEach(variable -> {
                List<ITypeBinding> types = Arrays.asList(variable.getType().getTypeArguments());
                if (types.size() > 0) {
                  attributes.add(new Pair<>(variable, types));
                }
              });
            });
      }
    };

    SearchEngine searchEngine = new SearchEngine();

    try {
      searchEngine.search(pattern,
          new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()}, scope, requestor,
          null);
    } catch (CoreException e) {
      e.printStackTrace();
    }

    return attributes;
  }

  public static List<IType> declaringClassUsages(MTypeParameter entity) throws JavaModelException {
    Optional<IType> maybeParentType = convert(entity.getUnderlyingObject().getDeclaringClass());

    if (maybeParentType.isPresent()) {
      ITypeHierarchy parentHierarchy =
          maybeParentType.get().newTypeHierarchy(new NullProgressMonitor());

      List<IType> allHierarchyUsages =
          Arrays.asList(parentHierarchy.getAllSubtypes(maybeParentType.get()));

      return allHierarchyUsages;
    }

    return Collections.emptyList();
  }

  public static List<IType> inheritanceUsages(MTypeParameter entity) throws JavaModelException {
    Optional<IType> maybeParentType = convert(entity.getUnderlyingObject().getDeclaringClass());
    Optional<IType> type = convert(entity.getUnderlyingObject().getSuperclass());
    BiFunction<String, List<IType>, Optional<IType>> transform = (name, types) -> types.stream()
        .filter(t -> t.getFullyQualifiedName().equals(name)).findFirst();

    if (maybeParentType.isPresent() && type.isPresent()) {
      ITypeHierarchy hierarchy = type.get().newTypeHierarchy(new NullProgressMonitor());
      ITypeHierarchy parentHierarchy =
          maybeParentType.get().newTypeHierarchy(new NullProgressMonitor());

      List<IType> allTypes = new ArrayList<>();
      allTypes.addAll(Arrays.asList(hierarchy.getAllSubtypes(type.get())));
      allTypes.addAll(getAllTypes(type.get().getJavaProject()));
      List<IType> allHierarchyUsages =
          Arrays.asList(parentHierarchy.getAllSubtypes(maybeParentType.get())).stream()
              .map(t -> convert(t))
              .map(o -> o.map(t -> Arrays.asList(t.getSuperclass().getTypeArguments())))
              .map(o -> o.map(l -> l.stream().map(t -> {
                Optional<IType> maybeType = transform.apply(t.getQualifiedName(), allTypes);
                System.out.println(t.getQualifiedName() + " -- " + maybeType);
                return maybeType.get();
              }).collect(Collectors.toList())).orElse(Collections.emptyList()))
              .flatMap(List::stream).collect(Collectors.toList());

      return allHierarchyUsages;
    }

    return Collections.emptyList();
  }

  public static List<IType> attributesUsages(MTypeParameter entity) throws JavaModelException {
    List<IType> types = new ArrayList<>();
    Optional<IType> maybeParentType = convert(entity.getUnderlyingObject().getDeclaringClass());
    IType parentType = maybeParentType.get();

    final List<List<ITypeBinding>> allParametersUsages = ITypeStore.findAttributeUsages(parentType)
        .stream().map(p -> p.getSecond()).collect(Collectors.toList());

    final List<ITypeParameter> parameters = Arrays.asList(parentType.getTypeParameters());
    IntStream.range(0, parameters.size())
        .filter(i -> parameters.get(i).getElementName()
            .equals(entity.getUnderlyingObject().getJavaElement().getElementName()))
        .findFirst()
        .ifPresent(indexOfCurrentType -> allParametersUsages.stream()
            .map(l -> l.get(indexOfCurrentType)).collect(Collectors.toList())
            .forEach(t -> ITypeStore.convert(t).ifPresent(t1 -> types.add(t1))));
    return types;
  }

  public static List<IType> allUsages(MTypeParameter entity) throws JavaModelException {
    List<IType> types = new ArrayList<>();
    if (entity.getUnderlyingObject().getSuperclass().getQualifiedName()
        .equals(Object.class.getCanonicalName())) {
      types.addAll(declaringClassUsages(entity));
    } else {
      types.addAll(inheritanceUsages(entity));
    }
    types.addAll(attributesUsages(entity));

    return types;
  }
}

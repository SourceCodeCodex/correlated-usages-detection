package upt.se.utils.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import upt.se.utils.visitors.GenericParameterBindingVisitor;
import upt.se.utils.visitors.HierarchyBindingVisitor;

public final class ITypeStore {
  private static Map<IJavaProject, List<IType>> allTypes = new HashMap<>();
  private static Map<IType, Optional<ITypeBinding>> typeCache = new HashMap<>();
  private static Map<ITypeBinding, Optional<IType>> typeBindingCache = new HashMap<>();

  public static final List<IType> getAllTypes(IJavaProject javaproject) {

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

    typeBindingCache.put(typeBinding,
        getAllTypes(typeBinding.getJavaElement().getJavaProject()).stream()
            .filter(t -> t.getFullyQualifiedName()
                .equals(typeBinding.isParameterizedType() ? typeBinding.getBinaryName()
                    : typeBinding.getQualifiedName()))
            .findFirst());

    Optional<IType> result = typeBindingCache.get(typeBinding);
    result.ifPresent(t -> typeCache.put(t, Optional.ofNullable(typeBinding)));

    return result;
  }

  public static final Optional<ITypeBinding> convert(IType type) {
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

}

package upt.se.utils.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;

import upt.se.utils.Pair;
import upt.se.utils.visitors.HierarchyBindingVisitor;

public final class ITypeStore {
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
		return typeList;
	}

	public static final Optional<IType> convert(ITypeBinding typeBinding) {
		return getAllTypes(typeBinding.getSuperclass().getJavaElement().getJavaProject()).stream()
				.filter(t -> t.getElementName().equals(typeBinding.getJavaElement().getElementName()))
				.findFirst();
	}

	public static final List<Pair<IType, List<ITypeBinding>>> getAllChildrenTypes(IType type) {
		try {
			ITypeHierarchy hierarchy = type.newTypeHierarchy(new NullProgressMonitor());

			List<IType> allSubtypes = Arrays.asList(hierarchy.getAllSubtypes(type));

			return allSubtypes.stream()
					.map(t -> HierarchyBindingVisitor.convert(t.getCompilationUnit()).stream()
							.filter(t1 -> t1.getJavaElement().getElementName().equals(t.getElementName())).findFirst()
							.map(t1 -> new Pair<>(t, Arrays.asList(t1.getSuperclass().getTypeArguments()))).get())
					.filter(p -> p.getSecond().size() > 0).collect(Collectors.toList());

		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}

package groups.xclass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import com.salexandru.xcore.utils.interfaces.Group;
import com.salexandru.xcore.utils.interfaces.IRelationBuilder;
import com.salexandru.xcore.utils.metaAnnotation.RelationBuilder;

import exampletool.metamodel.entity.XClass;
import exampletool.metamodel.entity.XTypeParameter;
import exampletool.metamodel.factory.Factory;
import visitors.HierarchyBindingVisitor;

@RelationBuilder
public class GenericParametersAllTypes implements IRelationBuilder<XTypeParameter, XClass> {

	@Override
	public Group<XTypeParameter> buildGroup(XClass entity) {
		List<ITypeBinding> possibleTypes = new ArrayList<>();
		try {
			List<IType> allTypes = getTypes(entity.getUnderlyingObject().getJavaProject());
			List<ITypeBinding> parameters = entity.genericParametersGroup().getElements().stream()
					.map(XTypeParameter::getUnderlyingObject).collect(Collectors.toList());

			List<IType> parametersType = allTypes.stream()
					.filter(t -> parameters.stream()
							.anyMatch(p -> p.getJavaElement().getElementName().equals(t.getElementName())))
					.collect(Collectors.toList());

			List<IType> superTypes = parametersType.stream().map(p -> {
				try {
					return Arrays.asList(p.newTypeHierarchy(new NullProgressMonitor()).getAllSupertypes(p));
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.toList()).stream().flatMap(List::stream).filter(t -> t.getCompilationUnit() != null)
					.collect(Collectors.toList());

			List<ITypeBinding> allTypeBindings = superTypes.stream().map(t -> t.getCompilationUnit())
					.map(c -> HierarchyBindingVisitor.convert(c)).collect(Collectors.toList()).stream()
					.flatMap(Set::stream).collect(Collectors.toList());

			List<ITypeBinding> hierarchyPossibleTypes = superTypes.stream()
					.map(type -> allTypeBindings.stream()
							.filter(t -> t.getJavaElement().getElementName().equals(type.getElementName())).findFirst()
							.get())
					.collect(Collectors.toList());

			possibleTypes.addAll(hierarchyPossibleTypes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Group<XTypeParameter> group = new Group<>();
		group.addAll(possibleTypes.stream().map(t -> Factory.getInstance().createXTypeParameter(t))
				.collect(Collectors.toList()));

		return group;
	}

	public final List<IType> getTypes(IJavaProject javaproject) {
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

}

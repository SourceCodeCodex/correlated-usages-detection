package groups.xclass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

import com.salexandru.xcore.utils.interfaces.IPropertyComputer;
import com.salexandru.xcore.utils.metaAnnotation.PropertyComputer;

import exampletool.metamodel.entity.XClass;
import utils.Pair;
import visitors.HierarchyBindingVisitor;

@PropertyComputer
public class GenericParametersApertureGroup implements IPropertyComputer<Double, XClass> {

	@SuppressWarnings("unchecked")
	@Override
	public Double compute(XClass entity) {
		Pair<List<ITypeBinding>, List<List<ITypeBinding>>> attributeUsages = entity
				.genericParametersAttributeUsagesGroup().getElements().get(0).getUnderlyingObject();
		Pair<List<ITypeBinding>, List<List<ITypeBinding>>> hierarchyUsages = entity
				.genericParametersHierarchyUsagesGroup().getElements().get(0).getUnderlyingObject();

		List<List<ITypeBinding>> allUsages = new ArrayList<>();
		allUsages.addAll(hierarchyUsages.getSecond());
		allUsages.addAll(attributeUsages.getSecond());
		try {
			// FIXME all the possible types should be a union between all the classes which
			// are not generic and extend the
			// current generic entity(a.k.a. HT := hierarchy type) and the subclasses of the
			// generic parameters if they
			// are bounded(a.k.a. PT := parameter types)
			// HT U PT =:= allPossibleTypes
			ITypeHierarchy newTypeHierarchy = entity.getUnderlyingObject().newTypeHierarchy(new NullProgressMonitor());
			// HT
			List<ITypeBinding> hierarchyPossibleTypes = Arrays
					.asList(newTypeHierarchy.getAllSubtypes(entity.getUnderlyingObject())).stream()
					.map(type -> HierarchyBindingVisitor.convert(type.getCompilationUnit()).stream()
							.filter(t -> t.getJavaElement().getElementName().equals(type.getElementName())).findFirst()
							.get())
					.collect(Collectors.toList());
			// PT
			List<IType> allTypes = getTypes(entity.getUnderlyingObject().getJavaProject());
			List<ITypeBinding> parametersPossibleTypes = entity.genericParametersGroup().getElements().stream()
					.filter(p -> p.getUnderlyingObject().getTypeBounds().length != 0)
					.map(p -> allTypes.stream()
							.filter(t -> t.getElementName()
									.equals(p.getUnderlyingObject().getSuperclass().getJavaElement().getElementName()))
							.findFirst()
							.get())
					.map(t -> Arrays.asList(newTypeHierarchy.getAllSubtypes(t)).stream()
							.map(type -> HierarchyBindingVisitor.convert(type.getCompilationUnit()).stream()
									.filter(t1 -> t1.getJavaElement().getElementName().equals(t.getElementName()))
									.findFirst()
									.get())
							.collect(Collectors.toList()))
					.flatMap(List::stream).collect(Collectors.toList());

			List<ITypeBinding> possibleTypes = new ArrayList<>();
			possibleTypes.addAll(hierarchyPossibleTypes);
			possibleTypes.addAll(parametersPossibleTypes);

			double allTypesCounter = new Double(possibleTypes.size());
			double usedTypesCounter = new Double(possibleTypes.stream()
					.filter(type -> allUsages.stream().anyMatch(usages -> usages.stream().anyMatch(usage -> usage
							.getJavaElement().getElementName().equals(type.getJavaElement().getElementName()))))
					.count());

			return (usedTypesCounter * 100d) / allTypesCounter;
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return 0d;
	}
	
	//FIXME possible naming; closed aperture |accepted aperture
	//TODO bug; hierarchy it is not counted as possible types(e.g. class Y extends X<String,Object>{...} Y is not part of all the possible types)
	//TODO create 2 group builders; one to return all the possible types ; the other to return all the used types
	//TODO inspect method of usages of generic class; analyze generic variable from method
	
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

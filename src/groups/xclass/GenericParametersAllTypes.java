package groups.xclass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ITypeHierarchy;
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
			ITypeHierarchy newTypeHierarchy = entity.getUnderlyingObject().newTypeHierarchy(new NullProgressMonitor());
			List<ITypeBinding> hierarchyPossibleTypes = Arrays
					.asList(newTypeHierarchy.getAllSubtypes(entity.getUnderlyingObject())).stream()
					.map(type -> HierarchyBindingVisitor.convert(type.getCompilationUnit()).stream()
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

}

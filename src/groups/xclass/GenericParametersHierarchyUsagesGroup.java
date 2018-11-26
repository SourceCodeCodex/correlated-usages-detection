package groups.xclass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;

import com.salexandru.xcore.utils.interfaces.Group;
import com.salexandru.xcore.utils.interfaces.IRelationBuilder;
import com.salexandru.xcore.utils.metaAnnotation.RelationBuilder;

import exampletool.metamodel.entity.XClass;
import exampletool.metamodel.entity.XPair;
import exampletool.metamodel.factory.Factory;
import utils.Pair;
import visitors.HierarchyBindingVisitor;

@RelationBuilder
public class GenericParametersHierarchyUsagesGroup implements IRelationBuilder<XPair, XClass> {

	@Override
	public Group<XPair> buildGroup(XClass entity) {
		Group<XPair> group = new Group<>();
		List<ITypeBinding> parameters = entity.genericParametersGroup().getElements().stream()
				.map(e -> e.getUnderlyingObject()).collect(Collectors.toList());
		try {
			ITypeHierarchy hierarchy = entity.getUnderlyingObject().newTypeHierarchy(new NullProgressMonitor());

			List<IType> allSubtypes = Arrays.asList(hierarchy.getAllSubtypes(entity.getUnderlyingObject()));
			List<List<ITypeBinding>> usages = allSubtypes.stream()
					.map(t -> HierarchyBindingVisitor.convert(t.getCompilationUnit()).stream()
							.filter(type -> type.getJavaElement().getElementName().equals(t.getElementName()))
							.findFirst().map(type -> Arrays.asList(type.getSuperclass().getTypeArguments())).get())
					.filter(list -> list.size() > 0).collect(Collectors.toList());

			Pair<List<ITypeBinding>, List<List<ITypeBinding>>> pair = new Pair<>(parameters, usages);

			group.add(Factory.getInstance().createXPair(pair));
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return group;
	}

}

package groups.xclass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ITypeBinding;

import com.salexandru.xcore.utils.interfaces.Group;
import com.salexandru.xcore.utils.interfaces.IRelationBuilder;
import com.salexandru.xcore.utils.metaAnnotation.RelationBuilder;

import exampletool.metamodel.entity.XClass;
import exampletool.metamodel.entity.XTypeParameter;
import exampletool.metamodel.factory.Factory;
import utils.Pair;

@RelationBuilder
public class GenericParametersAllUsages implements IRelationBuilder<XTypeParameter, XClass> {

	@SuppressWarnings("unchecked")
	@Override
	public Group<XTypeParameter> buildGroup(XClass entity) {

		Pair<List<ITypeBinding>, List<List<ITypeBinding>>> attributeUsages = entity
				.genericParametersAttributeUsagesGroup().getElements().get(0).getUnderlyingObject();
		Pair<List<ITypeBinding>, List<List<ITypeBinding>>> hierarchyUsages = entity
				.genericParametersHierarchyUsagesGroup().getElements().get(0).getUnderlyingObject();

		List<List<ITypeBinding>> allUsages = new ArrayList<>();
		allUsages.addAll(hierarchyUsages.getSecond());
		allUsages.addAll(attributeUsages.getSecond());

		Group<XTypeParameter> group = new Group<>();
		group.addAll(allUsages.stream().flatMap(List::stream).map(t -> Factory.getInstance().createXTypeParameter(t))
				.collect(Collectors.toList()));

		return group;
	}

}

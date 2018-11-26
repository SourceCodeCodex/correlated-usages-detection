package groups.xclass;

import java.util.List;

import org.eclipse.jdt.core.dom.ITypeBinding;

import com.salexandru.xcore.utils.interfaces.Group;
import com.salexandru.xcore.utils.interfaces.IRelationBuilder;
import com.salexandru.xcore.utils.metaAnnotation.RelationBuilder;

import exampletool.metamodel.entity.XClass;
import exampletool.metamodel.entity.XPair;
import exampletool.metamodel.factory.Factory;
import utils.Pair;

@RelationBuilder
public class GenericParametersAllUsages implements IRelationBuilder<XPair, XClass> {

	@SuppressWarnings("unchecked")
	@Override
	public Group<XPair> buildGroup(XClass entity) {

		Pair<List<ITypeBinding>, List<List<ITypeBinding>>> attributeUsages = entity
				.genericParametersAttributeUsagesGroup().getElements().get(0).getUnderlyingObject();
		Pair<List<ITypeBinding>, List<List<ITypeBinding>>> hierarchyUsages = entity
				.genericParametersHierarchyUsagesGroup().getElements().get(0).getUnderlyingObject();

		Pair<List<ITypeBinding>, List<List<ITypeBinding>>> allUsages = attributeUsages.clone();
		allUsages.getSecond().addAll(hierarchyUsages.getSecond());

		Group<XPair> group = new Group<>();
		group.getElements().add(Factory.getInstance().createXPair(allUsages));

		return group;
	}

}

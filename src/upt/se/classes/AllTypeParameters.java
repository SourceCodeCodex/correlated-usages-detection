package upt.se.classes;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ITypeBinding;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.visitors.GenericParameterBindingVisitor;

@RelationBuilder
public class AllTypeParameters implements IRelationBuilder<MTypeParameter, MClass> {

	@Override
	public Group<MTypeParameter> buildGroup(MClass entity) {
		HashSet<ITypeBinding> set = GenericParameterBindingVisitor
				.convert(entity.getUnderlyingObject().getCompilationUnit());

		Group<MTypeParameter> types = new Group<>();
		types.addAll(set.stream().map(type -> Factory.getInstance().createMTypeParameter(type))
				.collect(Collectors.toList()));
		
		return types;
	}

}

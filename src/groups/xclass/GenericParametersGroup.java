package groups.xclass;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ITypeBinding;

import com.salexandru.xcore.utils.interfaces.Group;
import com.salexandru.xcore.utils.interfaces.IRelationBuilder;
import com.salexandru.xcore.utils.metaAnnotation.RelationBuilder;

import exampletool.metamodel.entity.XClass;
import exampletool.metamodel.entity.XTypeParameter;
import exampletool.metamodel.factory.Factory;
import visitors.GenericParameterBindingVisitor;

@RelationBuilder
public class GenericParametersGroup implements IRelationBuilder<XTypeParameter, XClass> {

	@Override
	public Group<XTypeParameter> buildGroup(XClass entity) {
		HashSet<ITypeBinding> set = GenericParameterBindingVisitor
				.convert(entity.getUnderlyingObject().getCompilationUnit());

		Group<XTypeParameter> types = new Group<>();
		types.addAll(set.stream().map(type -> Factory.getInstance().createXTypeParameter(type))
				.collect(Collectors.toList()));

		return types;
	}

}

package upt.se.classes;

import org.eclipse.jdt.core.dom.ITypeBinding;

import static upt.se.utils.visitors.IType2ITypeDeclarationBindingConverter.convert;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MParameter;
import thesis.metamodel.factory.Factory;

@RelationBuilder
public class TypeParameters implements IRelationBuilder<MParameter, MClass> {

	@Override
	public Group<MParameter> buildGroup(MClass entity) {
		Group<MParameter> result = new Group<MParameter>();
		ITypeBinding typeBinding = convert(entity.getUnderlyingObject()).get();
		for (ITypeBinding aTypeParameter : typeBinding.getTypeParameters()) {
			result.add(Factory.getInstance().createMParameter(aTypeParameter));
		}
		return result;
	}
}


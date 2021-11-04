package upt.ac.cti.classes;

import java.util.stream.Stream;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MField;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;

@RelationBuilder
public class FieldGroup implements IRelationBuilder<MField, MClass> {

	@Override
	public Group<MField> buildGroup(MClass mClass) {
		var fieldGroup = new Group<MField>();
		var type = (IType) mClass.getUnderlyingObject();
		try {
			Stream<IField> iFields = Stream.of(type.getFields());
			iFields.map(it -> Factory.getInstance().createMField(it)).forEach(it -> fieldGroup.add(it));
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return fieldGroup;
	}

}
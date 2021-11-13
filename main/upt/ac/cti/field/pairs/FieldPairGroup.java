package upt.ac.cti.field.pairs;

import java.util.List;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.javatuples.Pair;

import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;

@RelationBuilder
public class FieldPairGroup implements IRelationBuilder<MFieldPair, MClass> {

	@Override
	public Group<MFieldPair> buildGroup(MClass mClass) {
		var fieldPairGroup = new Group<MFieldPair>();
		List<IField> iFields;
		try {
			iFields = List.of(((IType) mClass.getUnderlyingObject()).getFields());
		} catch (JavaModelException e) {
			e.printStackTrace();
			iFields = List.of();
		}
		var eligibleIFields = filterOutIneligible(iFields);
		var iFieldsCount = eligibleIFields.size();
		for (var i = 0; i < iFieldsCount; i++) {
			for (var j = i + 1; j < iFieldsCount; j++) {
				var pair = Pair.with(iFields.get(i), iFields.get(j));
				fieldPairGroup.add(Factory.getInstance().createMFieldPair(pair));
			}
		}
		return fieldPairGroup;
	}

	private List<IField> filterOutIneligible(List<IField> fields) {
		return fields.stream().filter(it -> {
			try {
				return !Flags.isStatic(it.getFlags());
			} catch (JavaModelException e) {
				e.printStackTrace();
				return false;
			}
		}).toList();
	}

}
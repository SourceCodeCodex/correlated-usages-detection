package upt.ac.cti.classes;

import java.util.stream.Stream;

import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;

import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MField;
import familypolymorphismdetection.metamodel.entity.MTypePair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.model.TypePair;

@RelationBuilder
public class TypePairGroup implements IRelationBuilder<MTypePair, MClass> {

	@Override
	public Group<MTypePair> buildGroup(MClass mClass) {
		var typePairGroup = new Group<MTypePair>();
		var mFields = mClass.fieldGroup();
		var fieldsStream = mFields.getElements().stream();
		var fieldPairsStream = combineBinary(fieldsStream);
		Stream<Pair<IType, IType>> subtypePairs = fieldPairsStream
				.map(pair -> Pair.with(getITypesOfField(pair.getValue0()), getITypesOfField(pair.getValue1())))
				.flatMap(pair -> cartesianProduct(pair.getValue0(), pair.getValue1()));
		var mTypePairs = subtypePairs.map(pair -> Factory.getInstance().createMTypePair(TypePair.of(pair)));
		mTypePairs.forEach(it -> typePairGroup.add(it));
		return typePairGroup;
	}

	private <T> Stream<Pair<T, T>> cartesianProduct(Stream<T> s1, Stream<T> s2) {
		return s1.flatMap(it -> s2.map(el -> Pair.with(it, el)));
	}

	private <T> Stream<Pair<T, T>> combineBinary(Stream<T> stream) {
		return stream.map(it -> Pair.with(it, stream.dropWhile(el -> el != it).filter(el -> el == it)))
				.flatMap(pair -> pair.getValue1().map(it -> Pair.with(it, pair.getValue0())));
	}

	private Stream<IType> getITypesOfField(MField mField) {
		return mField.subtypeGroup().getElements().stream().map(it -> (IType) it.getUnderlyingObject());
	}

}

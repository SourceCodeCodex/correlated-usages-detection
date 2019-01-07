package upt.se.pair;

import java.util.List;
import java.util.stream.IntStream;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MTypePair;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.TypePair;
import upt.se.utils.store.ITypeStore;

@RelationBuilder
public class ActualParameterTypes implements IRelationBuilder<MTypePair, MTypePair> {

	@Override
	public Group<MTypePair> buildGroup(MTypePair entity) {
		MTypeParameter firstParameter = Factory.getInstance()
				.createMTypeParameter(entity.getUnderlyingObject().getFirst());
		MTypeParameter secondParameter = Factory.getInstance()
				.createMTypeParameter(entity.getUnderlyingObject().getSecond());

		List<MClass> firstParameterUsages = firstParameter.allSubtypes().getElements();
		List<MClass> secondParameterUsages = secondParameter.allSubtypes().getElements();

		Group<MTypePair> group = new Group<>();

		if (firstParameterUsages.size() == secondParameterUsages.size()) {

			IntStream.range(0, firstParameterUsages.size())
					.forEach(i -> group.add(Factory.getInstance()
							.createMTypePair(new TypePair(
									ITypeStore.convert(firstParameterUsages.get(i).getUnderlyingObject()).get(),
									ITypeStore.convert(secondParameterUsages.get(i).getUnderlyingObject()).get()))));
		}
		return group;
	}

}

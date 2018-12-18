package upt.se.classes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MTypePair;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.TypePair;

@RelationBuilder
public class AllTypeParameterPairs implements IRelationBuilder<MTypePair, MClass> {

	@Override
	public Group<MTypePair> buildGroup(MClass entity) {
		Group<MTypeParameter> allTypeParameters = entity.allTypeParameters();
		List<MTypeParameter> elements = allTypeParameters.getElements();

		Group<MTypePair> allPairs = new Group<>();

		allPairs.addAll(IntStream.range(0, elements.size() - 1).mapToObj(i -> IntStream
				.range(i + 1, elements.size())
				.mapToObj(j -> Factory.getInstance().createMTypePair(
						new TypePair(elements.get(i).getUnderlyingObject(), elements.get(j).getUnderlyingObject())))
				.collect(Collectors.toList())).collect(Collectors.toList()).stream().flatMap(List::stream)
				.collect(Collectors.toList()));

		return allPairs;
	}

}

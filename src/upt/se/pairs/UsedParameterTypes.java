package upt.se.pairs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MTypePair;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.TypePair;
import upt.se.utils.store.ITypeStore;

@RelationBuilder
public class UsedParameterTypes implements IRelationBuilder<MTypePair, MTypePair> {

	@Override
	public Group<MTypePair> buildGroup(MTypePair entity) {
		MTypeParameter firstParameter = Factory.getInstance()
				.createMTypeParameter(entity.getUnderlyingObject().getFirst());
		MTypeParameter secondParameter = Factory.getInstance()
				.createMTypeParameter(entity.getUnderlyingObject().getSecond());
		Group<MTypePair> group = new Group<>();
		try {
			List<IType> firstParameterUsages = ITypeStore.allUsages(firstParameter);
			List<IType> secondParameterUsages = ITypeStore.allUsages(secondParameter);

			Set<TypePair> pairs = new HashSet<>();
			if (firstParameterUsages.size() == secondParameterUsages.size()) {
				IntStream.range(0, firstParameterUsages.size())
						.forEach(i -> pairs.add(new TypePair(ITypeStore.convert(firstParameterUsages.get(i)).get(),
								ITypeStore.convert(secondParameterUsages.get(i)).get())));

				group.addAll(pairs.stream().map(Factory.getInstance()::createMTypePair).distinct()
						.collect(Collectors.toSet()));
			}
			return removeDuplicates(group);
		} catch (JavaModelException ex) {
			ex.printStackTrace();
		}
		return group;
	}

	private Group<MTypePair> removeDuplicates(Group<MTypePair> group) {
		Group<MTypePair> result = new Group<>();

		for (MTypePair pair : group.getElements()) {
			if (!result.getElements().stream().anyMatch(p -> p.equals(pair))) {
				result.add(pair);
			}
		}

		return result;
	}

}

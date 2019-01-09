package upt.se.pair;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ITypeBinding;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MTypePair;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.TypePair;
import upt.se.utils.store.ITypeStore;

@RelationBuilder
public class AllParameterTypes implements IRelationBuilder<MTypePair, MTypePair> {

	@Override
	public Group<MTypePair> buildGroup(MTypePair entity) {
		MTypeParameter firstParameter = Factory.getInstance()
				.createMTypeParameter(entity.getUnderlyingObject().getFirst());
		MTypeParameter secondParameter = Factory.getInstance()
				.createMTypeParameter(entity.getUnderlyingObject().getSecond());

		Optional<List<ITypeBinding>> firstParameterSubtypes = ITypeStore.convert(firstParameter.allSubtypes()
				.getElements().stream().map(c -> c.getUnderlyingObject()).collect(Collectors.toList()));
		Optional<List<ITypeBinding>> secondParameterSubtypes = ITypeStore.convert(secondParameter.allSubtypes()
				.getElements().stream().map(c -> c.getUnderlyingObject()).collect(Collectors.toList()));

		Group<MTypePair> group = new Group<>();

		if (firstParameterSubtypes.isPresent() && secondParameterSubtypes.isPresent()) {
			Set<TypePair> pairs = new HashSet<>();
			for (ITypeBinding first : firstParameterSubtypes.get()) {
				for (ITypeBinding second : secondParameterSubtypes.get()) {
					group.add(Factory.getInstance().createMTypePair(new TypePair(first, second)));
				}
			}
			group.addAll(pairs.stream().map(Factory.getInstance()::createMTypePair).collect(Collectors.toSet()));
		}
		return group;
	}

}

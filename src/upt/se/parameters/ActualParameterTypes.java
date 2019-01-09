package upt.se.parameters;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.store.ITypeStore;

@RelationBuilder
public class ActualParameterTypes implements IRelationBuilder<MClass, MTypeParameter> {
	@Override
	public Group<MClass> buildGroup(MTypeParameter entity) {
		Group<MClass> group = new Group<>();
		try {
			if (entity.getUnderlyingObject().getSuperclass().getQualifiedName()
					.equals(Object.class.getName())) {

				return group;
			}

			Optional<IType> maybeParentType = ITypeStore.convert(entity.getUnderlyingObject().getDeclaringClass());
			Optional<IType> maybeCurrentType = ITypeStore.convert(entity.getUnderlyingObject().getSuperclass());
			if (maybeParentType.isPresent() && maybeCurrentType.isPresent()) {
				inheritanceUsages(entity, group, maybeParentType);
				attributesUsages(entity, group, maybeParentType);
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return group;
	}

	private void inheritanceUsages(MTypeParameter entity, Group<MClass> group, Optional<IType> maybeParentType)
			throws JavaModelException {
		IType parentType = maybeParentType.get();

		final List<List<ITypeBinding>> allParametersUsages = ITypeStore.getAllChildrenTypes(parentType).stream()
				.map(p -> p.getSecond()).collect(Collectors.toList());

		final List<ITypeParameter> parameters = Arrays.asList(parentType.getTypeParameters());
		IntStream.range(0, parameters.size())
				.filter(i -> parameters.get(i).getElementName()
						.equals(entity.getUnderlyingObject().getJavaElement().getElementName()))
				.findFirst().ifPresent(
						indexOfCurrentType -> allParametersUsages.stream().map(l -> l.get(indexOfCurrentType))
								.collect(Collectors.toList()).forEach(t -> ITypeStore.convert(t)
										.ifPresent(t1 -> group.add(Factory.getInstance().createMClass(t1)))));
	}
	
	private void attributesUsages(MTypeParameter entity, Group<MClass> group, Optional<IType> maybeParentType)
			throws JavaModelException {
		IType parentType = maybeParentType.get();

		final List<List<ITypeBinding>> allParametersUsages = ITypeStore.findAttributeUsages(parentType).stream()
				.map(p -> p.getSecond()).collect(Collectors.toList());

		final List<ITypeParameter> parameters = Arrays.asList(parentType.getTypeParameters());
		IntStream.range(0, parameters.size())
				.filter(i -> parameters.get(i).getElementName()
						.equals(entity.getUnderlyingObject().getJavaElement().getElementName()))
				.findFirst().ifPresent(
						indexOfCurrentType -> allParametersUsages.stream().map(l -> l.get(indexOfCurrentType))
								.collect(Collectors.toList()).forEach(t -> ITypeStore.convert(t)
										.ifPresent(t1 -> group.add(Factory.getInstance().createMClass(t1)))));
	}

}

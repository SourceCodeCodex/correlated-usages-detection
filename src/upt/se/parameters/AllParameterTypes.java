package upt.se.parameters;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.store.ITypeStore;

@RelationBuilder
public class AllParameterTypes implements IRelationBuilder<MClass, MTypeParameter> {

	@Override
	public Group<MClass> buildGroup(MTypeParameter entity) {
		Group<MClass> all = new Group<>();
		if (entity.getUnderlyingObject().getSuperclass().getQualifiedName().equals(Object.class.getCanonicalName())) {
			ITypeStore.convert(entity.getUnderlyingObject())
					.ifPresent(t -> all.add(Factory.getInstance().createMClass(t)));
			return all;
		}

		List<MClass> allSubtypes = ITypeStore.convert(entity.getUnderlyingObject().getSuperclass()).map(t -> {
			try {
				List<IType> subTypes = Arrays.asList(t.newTypeHierarchy(new NullProgressMonitor()).getAllSubtypes(t));
				List<IType> res = new LinkedList<>();
				res.add(t);
				res.addAll(subTypes);
				return res;
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
			return Collections.<IType>emptyList();
		}).map(list -> list.stream().map(t -> Factory.getInstance().createMClass(t)).collect(Collectors.toList()))
				.orElseGet(() -> {
					if (entity.getUnderlyingObject().getSuperclass().getJavaElement().getElementName()
							.equals(Object.class.getSimpleName())) {
						return ITypeStore
								.getAllTypes(
										entity.getUnderlyingObject().getSuperclass().getJavaElement().getJavaProject())
								.stream().map(t -> Factory.getInstance().createMClass(t)).collect(Collectors.toList());
					} else {
						return Collections.<MClass>emptyList();
					}
				});
		all.addAll(allSubtypes);

		return all;
	}

}

package upt.se.arguments.single;

import static upt.se.utils.helpers.Helper.isAbstract;
import static upt.se.utils.helpers.Equals.isObject;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import static upt.se.utils.helpers.LoggerHelper.NULL_PROGRESS_MONITOR;
import java.util.logging.Level;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.collection.*;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class AllPossibleArgumentTypes implements IRelationBuilder<MClass, MParameter> {

	@Override
	public Group<MClass> buildGroup(MParameter entity) {
		return Try.of(() -> entity.getUnderlyingObject())
				.map(parameter -> List.of(parameter.getInterfaces()).append(parameter.getSuperclass()))
				.map(superTypes -> superTypes.toSet())
				.map(superTypes -> superTypes.flatMap(type -> getAllSubtypes(type)))
				.map(allTypes -> allTypes.map(Factory.getInstance()::createMClass)).map(GroupBuilder::wrap).get();
	}

	private Set<IType> getAllSubtypes(ITypeBinding type) {
		if (!isObject(type)) {
			return Try.of(() -> (IType) type.getJavaElement())
					.mapTry(superType -> List
							.of(superType.newTypeHierarchy(NULL_PROGRESS_MONITOR).getAllSubtypes(superType))
							.append(superType).toSet().filter(tpe -> !isAbstract(tpe)))
					.onFailure(exception -> LOGGER.log(Level.SEVERE,
							"An error occurred while trying to get all the parameters for: " + type.getQualifiedName(),
							exception))
					.getOrElse(HashSet.empty());
		} else {
			return HashSet.empty();
		}
	}

}

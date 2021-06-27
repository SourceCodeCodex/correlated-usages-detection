package upt.se.parameters.single;

import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.logging.Level;
import org.eclipse.jdt.core.IType;
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
public class UnusedArgumentTypes implements IRelationBuilder<MClass, MParameter> {

	@Override
	public Group<MClass> buildGroup(MParameter entity) {
	    return Try.of(() -> entity)
	        .map(type -> {
	        	Set<IType> all = GroupBuilder.unwrapArguments(type.possibleConcreteTypes()).toSet();
	        	Set<IType> used = GroupBuilder.unwrapArguments(type.usedArgumentTypes()).toSet();
	        	
	        	return all.diff(used);
	        })
	        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
	        .orElse(() -> Try.success(HashSet.empty()))
	        .map(types -> types.map(Factory.getInstance()::createMClass))
	        .map(GroupBuilder::wrap)
	        .get();
	  }

}

package upt.se.arguments.single;

import static upt.se.utils.helpers.Equals.isEqual;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.logging.Level;
import org.eclipse.jdt.core.IType;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MArgument;
import thesis.metamodel.entity.MParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.crawlers.InheritanceArgumentTypes;
import upt.se.utils.crawlers.VariablesArgumentTypes;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class UsedArgumentTypes implements IRelationBuilder<MArgument, MParameter> {
  
  @Override
  public Group<MArgument> buildGroup(MParameter entity) {
    return Try.of(() -> InheritanceArgumentTypes.getUsages(entity))
        .map(usedTypes -> usedTypes.appendAll(VariablesArgumentTypes.getUsages(entity)))
        .map(list -> list.distinctBy((p1, p2) -> isEqual(p1, p2) ? 0 : 1))
        .map(typeBindings -> typeBindings.map(typeBinding -> (IType) typeBinding.getJavaElement()))
        .map(types -> types.map(Factory.getInstance()::createMArgument))
        .onFailure(exception -> LOGGER.log(Level.SEVERE,
            "An error occurred while trying to get all the parameters for: "
                + entity.getUnderlyingObject().getQualifiedName(),
            exception))
        .orElse(Try.success(List.empty()))
        .map(GroupBuilder::wrap)
        .get();
  }

}

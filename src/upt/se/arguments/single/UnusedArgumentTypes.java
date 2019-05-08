package upt.se.arguments.single;

import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.logging.Level;
import org.eclipse.jdt.core.IType;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MArgument;
import thesis.metamodel.entity.MParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class UnusedArgumentTypes implements IRelationBuilder<MArgument, MParameter> {

  @Override
  public Group<MArgument> buildGroup(MParameter entity) {
    return Try.of(() -> entity)
        .map(type -> Tuple.of(type.allPossibleArgumentTypes(), type.usedArgumentTypes()))
        .map(tuple -> tuple.map(GroupBuilder::unwrapArguments, GroupBuilder::unwrapArguments))
        .map(tuple -> diff(tuple._1, tuple._2))
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(List.empty()))
        .map(types -> types.map(Factory.getInstance()::createMArgument))
        .map(GroupBuilder::wrap)
        .get();
  }

  private List<IType> diff(List<IType> l1, List<IType> l2) {
    return l1.toSet().diff(l2.toSet()).toList();
  }

}

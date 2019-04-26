package upt.se.arguments.single;

import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.logging.Level;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MArgumentType;
import thesis.metamodel.factory.Factory;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class UnusedArgumentTypes implements IRelationBuilder<MArgumentType, MArgumentType> {

  @Override
  public Group<MArgumentType> buildGroup(MArgumentType entity) {
    return Try.of(() -> entity)
        .map(type -> Tuple.of(type.allArgumentTypes(), type.usedArgumentTypes()))
        .map(tuple -> tuple.map(GroupBuilder::unwrapArguments, GroupBuilder::unwrapArguments))
        .map(tuple -> diff(tuple._1, tuple._2))
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(List.empty()))
        .map(types -> types.map(Factory.getInstance()::createMArgumentType))
        .map(GroupBuilder::wrap)
        .get();
  }
  
  private List<ITypeBinding> diff(List<ITypeBinding> l1, List<ITypeBinding> l2) {
    return l1.toSet().diff(l2.toSet()).toList();
  }

}

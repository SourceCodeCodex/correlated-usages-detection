package upt.se.arguments.pair;

import static io.vavr.API.For;
import static upt.se.utils.helpers.Equals.isEqual;
import static upt.se.utils.helpers.Equals.parentExtendsObject;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.logging.Level;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MTypePair;
import thesis.metamodel.factory.Factory;
import upt.se.utils.TypePair;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class AllArgumentsTypes implements IRelationBuilder<MTypePair, MTypePair> {

  @Override
  public Group<MTypePair> buildGroup(MTypePair entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .flatMap(parameters -> For(getAllParameterTypes(parameters.getFirst()),
            getAllParameterTypes(parameters.getSecond()))
                .yield((first, second) -> first.crossProduct(second)))
        .map(pairs -> pairs.distinctBy((p1, p2) -> isEqual(p1, p2) ? 0 : 1))
        .map(pairs -> pairs.toList())
        .map(pairs -> pairs.map(pair -> new TypePair(pair._1, pair._2))
            .map(Factory.getInstance()::createMTypePair))
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(List.empty()))
        .map(GroupBuilder::wrap)
        .get();
  }

  private Try<List<ITypeBinding>> getAllParameterTypes(ITypeBinding parameter) {
    return Try.of(() -> parameter)
        .map(Factory.getInstance()::createMArgumentType)
        .map(mTypeParameter -> parentExtendsObject(mTypeParameter)
            ? mTypeParameter.usedArgumentTypes()
            : mTypeParameter.allArgumentTypes())
        .map(GroupBuilder::unwrapArguments);
  }

}

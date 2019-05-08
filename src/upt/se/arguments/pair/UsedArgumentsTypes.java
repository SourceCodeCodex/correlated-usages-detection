package upt.se.arguments.pair;

import static upt.se.utils.helpers.Equals.isEqual;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.logging.Level;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClassPair;
import thesis.metamodel.entity.MParameter;
import thesis.metamodel.entity.MParameterPair;
import thesis.metamodel.factory.Factory;
import upt.se.utils.ArgumentPair;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class UsedArgumentsTypes implements IRelationBuilder<MClassPair, MParameterPair> {

  @Override
  public Group<MClassPair> buildGroup(MParameterPair entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .map(parameterPair -> Tuple.of(parameterPair.getFirst(), parameterPair.getSecond()))
        .map(parameterPair -> parameterPair.map(Factory.getInstance()::createMParameter,
            Factory.getInstance()::createMParameter))
        .map(mPairParameter -> mPairParameter.map(MParameter::usedArgumentTypes,
            MParameter::usedArgumentTypes))
        .map(usedArgumentPairs -> usedArgumentPairs.map(GroupBuilder::unwrapArguments,
            GroupBuilder::unwrapArguments))
        .map(usedArgumentPairs -> usedArgumentPairs.apply((firstUsedArguments,
            secondUsedArguments) -> firstUsedArguments.zip(secondUsedArguments)))
        .map(usedArgumentPairs -> usedArgumentPairs
            .distinctBy((first, second) -> isEqual(first, second) ? 0 : 1))
        .map(usedArgumentPairs -> usedArgumentPairs.map(pair -> new ArgumentPair(pair._1, pair._2)))
        .map(
            usedArgumentPairs -> usedArgumentPairs.map(Factory.getInstance()::createMClassPair))
        .onFailure(exception -> LOGGER.log(Level.SEVERE,
            "An error occurred while trying to get all the parameters for: "
                + entity.getUnderlyingObject().getFirst().getQualifiedName()
                + ", and " + entity.getUnderlyingObject().getSecond().getQualifiedName(),
            exception))
        .orElse(() -> Try.success(List.empty()))
        .map(GroupBuilder::wrap)
        .get();
  }

}

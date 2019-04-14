package upt.se.pairs;

import static io.vavr.API.For;
import static upt.se.utils.builders.ListBuilder.toList;
import static upt.se.utils.helpers.ClassNames.isEqual;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.Collections;
import java.util.logging.Level;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MArgumentType;
import thesis.metamodel.entity.MTypePair;
import thesis.metamodel.factory.Factory;
import upt.se.utils.TypePair;
import upt.se.utils.builders.GroupBuilder;
import upt.se.utils.builders.ListBuilder;

@RelationBuilder
public class UsedArgumentsTypes implements IRelationBuilder<MTypePair, MTypePair> {

  @Override
  public Group<MTypePair> buildGroup(MTypePair entity) {
    return For(getUsedArgumentsTypes(entity), getDeclaringClassesArgumentsTypes(entity))
      .yield((usedTypes, declaringClassesArgumentTypes) -> declaringClassesArgumentTypes
          .filter(arguments -> arguments.find(argument -> usedTypes._1.find(type -> isEqual(type, argument._1)).isDefined() ||
                                                          usedTypes._2.find(type -> isEqual(type, argument._1)).isDefined())
                                        .isDefined())
          .flatMap(usedArguments -> getParametersPosition(entity)
                                    .map(parameters -> Tuple.of(usedArguments, parameters)))
          .map(tuple -> tuple._1.filter(argument -> tuple._2.find(parameter -> parameter._2 == argument._2)
                                                            .isDefined())))
      .map(usedArguments -> usedArguments.map(arguments -> arguments.map(argument -> argument._1)))
      .map(usedArguments -> usedArguments.map(arguments -> Tuple.of(arguments.head(), arguments.tail().head())))
      .map(pairs -> pairs.map(pair -> new TypePair(pair._1, pair._2))
                         .map(Factory.getInstance()::createMTypePair))
      .map(list -> list.asJava())
      .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
      .orElse(() -> Try.success(Collections.emptyList()))
      .map(GroupBuilder::wrap)
      .get(); 
  }

  private Try<List<Tuple2<ITypeBinding,Integer>>> getParametersPosition(MTypePair entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .map(parameters -> Tuple.of(parameters, parameters.getFirst().getDeclaringClass()))
        .map(tuple -> toList(tuple._2.getTypeParameters()).zipWithIndex()
                                                          .filter(parameter -> isEqual(tuple._1.getFirst(), parameter._1) ||
                                                                               isEqual(tuple._1.getSecond(), parameter._1)));
  }
  
  private Try<Tuple2<List<ITypeBinding>,List<ITypeBinding>>> getUsedArgumentsTypes(MTypePair entity) {
    return Try.of(() -> entity.getUnderlyingObject())
      .map(parameters -> Tuple.of(parameters.getFirst(), parameters.getSecond()))
      .map(tuple -> tuple.map(Factory.getInstance()::createMArgumentType, Factory.getInstance()::createMArgumentType))
      .map(tuple -> tuple.map(MArgumentType::usedArgumentTypes,MArgumentType::usedArgumentTypes))
      .map(tuple -> tuple.map(GroupBuilder::unwrap, GroupBuilder::unwrap)
                         .map(ListBuilder::toList, ListBuilder::toList));
  }
  
  private Try<List<List<Tuple2<ITypeBinding,Integer>>>> getDeclaringClassesArgumentsTypes(MTypePair entity) {
    return Try.of(() -> entity.getUnderlyingObject())
      .map(parameters -> Tuple.of(parameters.getFirst(), parameters.getSecond()))
      .filter(tuple -> isEqual(tuple._1.getDeclaringClass(), tuple._2.getDeclaringClass()))
      .map(tuple -> tuple._1.getDeclaringClass())
      .map(Factory.getInstance()::createMArgumentType)
      .map(MArgumentType::usedArgumentTypes)
      .map(GroupBuilder::unwrap)
      .map(declaringClasses -> toList(declaringClasses)
                               .map(declaringClass -> toList(declaringClass.getTypeArguments())
                                                      .zipWithIndex()))
      .onSuccess(list -> LOGGER.log(Level.INFO, list.toString()));
  }

}

package upt.se.pairs;

import static io.vavr.API.For;
import static upt.se.utils.builders.ListBuilder.toList;
import static upt.se.utils.helpers.ClassNames.*;
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
import thesis.metamodel.entity.MTypePair;
import thesis.metamodel.factory.Factory;
import upt.se.utils.TypePair;
import upt.se.utils.builders.GroupBuilder;
import upt.se.utils.store.ClassBindingStore;

@RelationBuilder
public class UsedArgumentsTypes implements IRelationBuilder<MTypePair, MTypePair> {

  @Override
  public Group<MTypePair> buildGroup(MTypePair entity) {
    return For(getDeclaringClassesArgumentsTypes(entity), getAttributesArgumentsTypes(entity))
      .yield((declaringClasses, attributeDeclarations) -> declaringClasses.appendAll(attributeDeclarations))
      .map(usedArguments -> usedArguments.map(arguments -> arguments.map(argument -> argument._1)))
      .map(usedArguments -> usedArguments.map(arguments -> Tuple.of(arguments.head(), arguments.tail().head())))
      .map(pairs -> pairs.distinctBy((p1, p2) -> isEqual(p1, p2) ?  0 : 1))
      .map(pairs -> pairs.map(pair -> new TypePair(pair._1, pair._2))
                         .map(Factory.getInstance()::createMTypePair))
      .map(list -> list.asJava())
      .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
      .orElse(() -> Try.success(Collections.emptyList()))
      .map(GroupBuilder::wrap)
      .get(); 
  }

  private Try<List<List<Tuple2<ITypeBinding,Integer>>>> getDeclaringClassesArgumentsTypes(MTypePair entity) {
    return Try.of(() -> entity.getUnderlyingObject())
      .map(parameters -> Tuple.of(parameters.getFirst(), parameters.getSecond()))
      .filter(tuple -> isEqual(tuple._1.getDeclaringClass(), tuple._2.getDeclaringClass()))
      .map(tuple -> tuple._1.getDeclaringClass())
      .map(ClassBindingStore::getAllSubtypes)
      .map(declaringClasses -> toList(declaringClasses)
                               .map(declaringClass -> declaringClass.getSuperclass())
                               .map(superClass -> toList(superClass.getTypeArguments())
                                                      .zipWithIndex()));
  }
  
  private Try<List<List<Tuple2<ITypeBinding,Integer>>>> getAttributesArgumentsTypes(MTypePair entity) {
    return Try.of(() -> entity.getUnderlyingObject())
      .map(parameters -> Tuple.of(parameters.getFirst(), parameters.getSecond()))
      .map(tuple -> tuple.map(Factory.getInstance()::createMArgumentType, Factory.getInstance()::createMArgumentType))
      .map(tuple -> tuple.map(ClassBindingStore::usagesInVariables, ClassBindingStore::usagesInVariables))
      .map(tuple -> toList(tuple._1).zip(tuple._2))
      .map(list -> list.map(tuple -> List.of(tuple._1, tuple._2)
                                         .zipWithIndex()));
  }
}

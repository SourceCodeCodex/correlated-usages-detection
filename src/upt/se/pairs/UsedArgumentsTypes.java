package upt.se.pairs;

import static io.vavr.API.For;
import static upt.se.utils.builders.ListBuilder.toList;
import static upt.se.utils.helpers.ClassNames.isEqual;
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
import upt.se.utils.builders.GroupBuilder;
import upt.se.utils.builders.ListBuilder;

@RelationBuilder
public class UsedArgumentsTypes implements IRelationBuilder<MTypePair, MTypePair> {

  @Override
  public Group<MTypePair> buildGroup(MTypePair entity) {
    For(getUsedArgumentsTypes(entity), getDeclaringClassesArgumentsTypes(entity))
      .yield((usedTypes, declaringClassesArgumentTypes) -> null); 
    
    return null;
  }
  
  private Try<Tuple2<List<ITypeBinding>,List<ITypeBinding>>> getUsedArgumentsTypes(MTypePair entity) {
    return Try.of(() -> entity.getUnderlyingObject())
      .map(parameters -> Tuple.of(parameters.getFirst(), parameters.getSecond()))
      .map(tuple -> tuple.map(Factory.getInstance()::createMArgumentType, Factory.getInstance()::createMArgumentType))
      .map(tuple -> tuple.map(MArgumentType::usedArgumentTypes,MArgumentType::usedArgumentTypes))
      .map(tuple -> tuple.map(GroupBuilder::unwrap, GroupBuilder::unwrap)
                         .map(ListBuilder::toList, ListBuilder::toList));
  }
  
  private Try<List<Tuple2<ITypeBinding,Integer>>> getDeclaringClassesArgumentsTypes(MTypePair entity) {
    return Try.of(() -> entity.getUnderlyingObject())
      .map(parameters -> Tuple.of(parameters.getFirst(), parameters.getSecond()))
      .filter(tuple -> isEqual(tuple._1.getDeclaringClass(), tuple._2.getDeclaringClass()))
      .map(tuple -> tuple._1.getDeclaringClass())
      .map(Factory.getInstance()::createMArgumentType)
      .map(MArgumentType::usedArgumentTypes)
      .map(GroupBuilder::unwrap)
      .map(declaringClasses -> toList(declaringClasses).flatMap(declaringClass -> toList(declaringClass.getTypeArguments()).zipWithIndex()));
  }

}

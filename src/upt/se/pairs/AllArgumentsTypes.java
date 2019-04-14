package upt.se.pairs;

import static io.vavr.API.For;
import static upt.se.utils.helpers.ClassNames.isEqual;
import static upt.se.utils.helpers.ClassNames.isObject;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.Collections;
import java.util.logging.Level;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.Tuple;
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
public class AllArgumentsTypes implements IRelationBuilder<MTypePair, MTypePair> {

  @Override
  public Group<MTypePair> buildGroup(MTypePair entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .flatMap(parameters -> For(getAllParameterTypes(parameters.getFirst()), 
                                   getAllParameterTypes(parameters.getSecond())).yield((first, second) -> Tuple.of(first, second)))
        .map(tuple -> tuple.map(GroupBuilder::unwrap, GroupBuilder::unwrap)
                           .map(ListBuilder::toList, ListBuilder::toList))
        .map(tuple -> tuple._1.crossProduct(tuple._2))
        .map(pairs -> pairs.distinctBy((p1, p2) -> isEqual(p1, p2) ?  0 : 1))
        .map(pairs -> pairs.map(pair -> new TypePair(pair._1, pair._2))
                           .map(Factory.getInstance()::createMTypePair))
        .map(pairs -> pairs.toJavaList())
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(Collections.emptyList()))
        .map(GroupBuilder::wrap)
        .get();
  }
  
  private Try<Group<MArgumentType>> getAllParameterTypes(ITypeBinding parameter) {
    return Try.of(() -> parameter)
        .map(Factory.getInstance()::createMArgumentType)
        .map(mTypeParameter -> isObject(mTypeParameter) ? mTypeParameter.allArgumentTypes(): 
                                                          mTypeParameter.usedArgumentTypes());
  }

}

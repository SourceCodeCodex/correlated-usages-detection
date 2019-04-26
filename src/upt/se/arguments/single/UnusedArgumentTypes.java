package upt.se.arguments.single;

import java.util.Collections;
import java.util.logging.Level;
import io.vavr.Tuple;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MArgumentType;
import thesis.metamodel.factory.Factory;
import upt.se.utils.builders.GroupBuilder;
import static upt.se.utils.builders.ListBuilder.*;
import static upt.se.utils.helpers.LoggerHelper.*;

@RelationBuilder
public class UnusedArgumentTypes implements IRelationBuilder<MArgumentType, MArgumentType> {
    
    @Override
    public Group<MArgumentType> buildGroup(MArgumentType entity) {
      return Try.of(() -> entity)
                .map(type -> Tuple.of(type.allArgumentTypes(), type.usedArgumentTypes()))
                .map(tuple -> tuple.map(GroupBuilder::unwrap, GroupBuilder::unwrap))
                .map(tuple -> diff(tuple._1, tuple._2))
                .map(types -> toList(types).map(Factory.getInstance()::createMArgumentType).asJava())
                .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
                .orElse(() -> Try.success(Collections.emptyList()))
                .map(GroupBuilder::wrap)
                .get();
    }
    
}
package upt.se.parameters;

import static upt.se.utils.builders.ListBuilder.diff;
import static upt.se.utils.builders.ListBuilder.toList;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.Collections;
import java.util.logging.Level;
import io.vavr.Tuple;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.builders.GroupBuilder;

@RelationBuilder
public class UnusedParameterTypes implements IRelationBuilder<MTypeParameter, MTypeParameter> {
    
    @Override
    public Group<MTypeParameter> buildGroup(MTypeParameter entity) {
      return Try.of(() -> entity)
                .map(type -> Tuple.of(type.allParameterTypes(), type.usedParameterTypes()))
                .map(tuple -> tuple.map(GroupBuilder::unwrap, GroupBuilder::unwrap))
                .map(tuple -> diff(tuple._1, tuple._2))
                .map(types -> toList(types).map(Factory.getInstance()::createMTypeParameter).asJava())
                .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
                .orElse(() -> Try.success(Collections.emptyList()))
                .map(GroupBuilder::wrap)
                .get();
    }
    
}
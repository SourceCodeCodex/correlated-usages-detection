package upt.se.classes;

import static upt.se.utils.helpers.Converter.convert;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.logging.Level;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MArgumentType;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.factory.Factory;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class AllTypeParameters implements IRelationBuilder<MArgumentType, MClass> {

  @Override
  public Group<MArgumentType> buildGroup(MClass entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .flatMap(type -> convert(type).toTry())
        .map(type -> type.getTypeParameters())
        .map(List::of)
        .map(parameters -> parameters.map(Factory.getInstance()::createMArgumentType))
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(List.empty()))
        .map(GroupBuilder::wrap)
        .get();
  }

}

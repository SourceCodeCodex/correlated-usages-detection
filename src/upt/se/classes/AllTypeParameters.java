package upt.se.classes;

import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.logging.Level;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MArgumentType;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.factory.Factory;
import upt.se.utils.builders.GroupBuilder;
import upt.se.utils.builders.ListBuilder;
import upt.se.utils.visitors.GenericParameterBindingVisitor;

@RelationBuilder
public class AllTypeParameters implements IRelationBuilder<MArgumentType, MClass> {

  @Override
  public Group<MArgumentType> buildGroup(MClass entity) {
    return Try.of(() -> entity.getUnderlyingObject().getCompilationUnit())
        .map(GenericParameterBindingVisitor::convert)
        .map(ListBuilder::toList)
        .filter(parameters -> parameters.groupBy(parameter -> parameter.getDeclaringClass().getQualifiedName()).size() == 1)
        .map(parameters -> parameters.head().getDeclaringClass().getTypeParameters())
        .map(ListBuilder::toList)
        .map(list -> list.map(Factory.getInstance()::createMArgumentType).asJava())
        .map(GroupBuilder::wrap)
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(new Group<>()))
        .get();
  }

}

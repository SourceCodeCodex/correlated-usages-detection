package upt.se.classes;

import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.builders.GroupBuilder;
import upt.se.utils.visitors.GenericParameterBindingVisitor;

@RelationBuilder
public class AllTypeParameters implements IRelationBuilder<MTypeParameter, MClass> {

  @Override
  public Group<MTypeParameter> buildGroup(MClass entity) {
    return Try.of(() -> entity.getUnderlyingObject().getCompilationUnit())
        .map(GenericParameterBindingVisitor::convert)
        .map(Set::stream)
        .map(stream -> stream.map(Factory.getInstance()::createMTypeParameter))
        .map(stream -> stream.collect(Collectors.toList()))
        .map(GroupBuilder::create)
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(new Group<>()))
        .get();
  }

}

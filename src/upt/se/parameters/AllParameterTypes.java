package upt.se.parameters;

import static upt.se.utils.helpers.ClassNames.getFullName;
import static upt.se.utils.helpers.ClassNames.isObject;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.Collections;
import java.util.logging.Level;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.builders.GroupBuilder;
import upt.se.utils.store.ITypeBindingStore;

@RelationBuilder
public class AllParameterTypes implements IRelationBuilder<MTypeParameter, MTypeParameter> {

  @Override
  public Group<MTypeParameter> buildGroup(MTypeParameter entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .filter(type -> isObject(getFullName(type.getSuperclass())))
        .fold(object -> Try.success(Collections.<ITypeBinding>emptyList()),
            type -> Try.of(() -> ITypeBindingStore.getAllSubtypes(type))
                .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t)))
        .map(List::ofAll)
        .map(types -> types.map(Factory.getInstance()::createMTypeParameter))
        .map(List::toJavaList)
        .map(GroupBuilder::wrap)
        .orElse(() -> Try.success(GroupBuilder.wrap(Collections.emptyList())))
        .get();
  }

}

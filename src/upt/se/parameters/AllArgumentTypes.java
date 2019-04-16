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
import thesis.metamodel.entity.MArgumentType;
import thesis.metamodel.factory.Factory;
import upt.se.utils.builders.GroupBuilder;
import upt.se.utils.builders.ListBuilder;
import upt.se.utils.store.ITypeBindingStore;

@RelationBuilder
public class AllArgumentTypes implements IRelationBuilder<MArgumentType, MArgumentType> {

  @Override
  public Group<MArgumentType> buildGroup(MArgumentType entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .map(type -> type.getSuperclass())
        .filter(type -> !isObject(getFullName(type)))
        .fold(object -> Try.success(Collections.<ITypeBinding>emptyList()),
            type -> Try.of(() -> ListBuilder.toList(ITypeBindingStore.getAllSubtypes(type))
                                            .prepend(entity.getUnderlyingObject().getSuperclass())
                                            .asJava())
                .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t)))
        .map(List::ofAll)
        .map(types -> types.map(Factory.getInstance()::createMArgumentType))
        .map(List::toJavaList)
        .orElse(() -> Try.success(Collections.emptyList()))
        .map(GroupBuilder::wrap)
        .get();
  }

}

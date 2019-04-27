package upt.se.arguments.single;

import static upt.se.utils.helpers.Equals.*;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import static upt.se.utils.helpers.LoggerHelper.NULL_PROGRESS_MONITOR;
import java.util.logging.Level;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.Tuple;
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
public class AllArgumentTypes implements IRelationBuilder<MClass, MArgumentType> {

  @Override
  public Group<MClass> buildGroup(MArgumentType entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .map(parameter -> List.of(parameter.getInterfaces()).append(parameter.getSuperclass()))
        .map(superTypes -> isObject(superTypes.head())
            ? superTypes
            : superTypes.filter(type -> !isObject(type)))
        .map(superTypes -> superTypes.flatMap(this::getAllSubtypes))
        .map(allTypes -> allTypes.map(Factory.getInstance()::createMClass))
        .map(GroupBuilder::wrap)
        .get();
  }

  public List<IType> getAllSubtypes(ITypeBinding type) {
    return Try.of(() -> type)
        .map(superType -> (IType) superType.getJavaElement())
        .mapTry(superType -> Tuple.of(superType,
            List.of(superType.newTypeHierarchy(NULL_PROGRESS_MONITOR).getAllSubtypes(superType))))
        .map(types -> types.apply((rootType, subTypes) -> subTypes.prepend(rootType)))
        .map(types -> types.sortBy(subType -> subType.getFullyQualifiedName()))
        .onFailure(exception -> LOGGER.log(Level.SEVERE,
            "An error occurred while trying to get all the parameters for: "
                + type.getQualifiedName(),
            exception))
        .orElse(Try.success(List.empty()))
        .get();
  }

}

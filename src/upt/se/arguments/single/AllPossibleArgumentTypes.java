package upt.se.arguments.single;

import static upt.se.utils.helpers.Equals.isObject;
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
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class AllPossibleArgumentTypes implements IRelationBuilder<MClass, MParameter> {

  @Override
  public Group<MClass> buildGroup(MParameter entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .map(parameter -> Tuple.of(List.of(parameter.getInterfaces()), parameter.getSuperclass()))
        .map(superTypes -> superTypes
            .apply((interfaces, superClass) -> getAllSubtypes(interfaces, superClass)))
        .map(allTypes -> allTypes.map(Factory.getInstance()::createMClass))
        .map(GroupBuilder::wrap)
        .get();
  }

  private List<IType> getAllSubtypes(List<ITypeBinding> interfaces, ITypeBinding superClass) {
    if (interfaces.isEmpty()) {
      return getAllSubtypes(superClass);
    } else {
      List<IType> interfaceSubtypes = interfaces.flatMap(type -> getAllSubtypes(type));
      if (!isObject(superClass)) {
        List<IType> classSubtypes = getAllSubtypes(superClass);

        return interfaceSubtypes.toSet().intersect(classSubtypes.toSet()).toList();
      }

      return interfaceSubtypes;
    }
  }

  private List<IType> getAllSubtypes(ITypeBinding type) {
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

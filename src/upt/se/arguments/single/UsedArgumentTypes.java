package upt.se.arguments.single;

import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.logging.Level;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;

import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.crawlers.InheritanceArgumentTypes;
import upt.se.utils.crawlers.VariablesArgumentTypes;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class UsedArgumentTypes implements IRelationBuilder<MClass, MParameter> {

  @Override
  public Group<MClass> buildGroup(MParameter entity) {
    return Try.of(() -> getInheritanceUsages(entity))
        .map(usedTypes -> usedTypes.appendAll(VariablesArgumentTypes.getUsages(entity)))
        .map(usedTypes -> usedTypes.distinctBy(type -> type.getQualifiedName()))
        .map(typeBindings -> typeBindings
            .map(typeBinding -> (IType) typeBinding.getErasure().getJavaElement()))
        .map(types -> types.distinctBy(type -> type.getFullyQualifiedName()))
        .map(types -> types.map(Factory.getInstance()::createMClass))
        .onFailure(exception -> LOGGER.log(Level.SEVERE,
            "An error occurred while trying to get all the parameters for: "
                + entity.getUnderlyingObject().getQualifiedName(),
            exception))
        .orElse(Try.success(List.empty()))
        .map(GroupBuilder::wrap)
        .get();
  }
  
  private List<ITypeBinding> getInheritanceUsages(MParameter parameter) {
	  List<List<ITypeBinding>> usages = InheritanceArgumentTypes.getUsages(parameter);
	  int paramPosition = InheritanceArgumentTypes.getParameterNumber(parameter.getUnderlyingObject());
	  return usages.map(arguments -> arguments.get(paramPosition));
  }

}

package upt.ac.cti.core.type.group;

import java.util.List;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MMethod;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;

@RelationBuilder
public final class Methods implements IRelationBuilder<MMethod, MClass> {

  @Override
  public Group<MMethod> buildGroup(MClass mClass) {
    var group = new Group<MMethod>();
    var type = (IType) mClass.getUnderlyingObject();
    List<IMethod> methods;

    try {
      methods = List.of(type.getMethods());
    } catch (JavaModelException e) {
      e.printStackTrace();

      methods = List.of();
    }

    var factory = Factory.getInstance();

    group.addAll(methods.stream().map(m -> factory.createMMethod(m)).toList());

    return group;
  }

}

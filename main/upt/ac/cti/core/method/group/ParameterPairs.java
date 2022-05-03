package upt.ac.cti.core.method.group;

import java.util.List;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MMethod;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.util.binding.ParameterTypeBindingResolver;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;
import upt.ac.cti.util.validation.ParameterValidator;

@RelationBuilder
public class ParameterPairs implements IRelationBuilder<MParameterPair, MMethod> {

  @Override
  public Group<MParameterPair> buildGroup(MMethod mMethod) {
    var group = new Group<MParameterPair>();
    var method = (IMethod) mMethod.getUnderlyingObject();

    List<ILocalVariable> parameters;

    try {
      parameters = List.of(method.getParameters());
    } catch (JavaModelException e) {
      e.printStackTrace();

      parameters = List.of();
    }

    var validator = new ParameterValidator(new ParameterTypeBindingResolver(),
        new ConcreteDescendantsResolver());

    var validParameters = parameters.stream()
        .filter(parameter -> validator.test(parameter))
        .toList();

    var paramsCount = validParameters.size();

    var factory = Factory.getInstance();

    for (var i = 0; i < paramsCount; i++) {
      for (var j = i + 1; j < paramsCount; j++) {
        var pair = Pair.with(validParameters.get(i), validParameters.get(j));
        group.add(factory.createMParameterPair(pair));
      }
    }

    return group;
  }

}

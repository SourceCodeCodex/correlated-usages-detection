package upt.ac.cti.core.type.group;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.dependency.Dependencies;

@RelationBuilder
public class SusceptibleParameterPairs implements IRelationBuilder<MParameterPair, MClass> {

  @Override
  public Group<MParameterPair> buildGroup(MClass mClass) {
    var group = new Group<MParameterPair>();

    var type = (IType) mClass.getUnderlyingObject();
    List<IMethod> methods;

    try {
      methods = List.of(type.getMethods());
    } catch (JavaModelException e) {
      e.printStackTrace();

      methods = List.of();
    }


    methods.stream()
        .flatMap(this::susceptibleParameterPairs)
        .forEach(pp -> group.add(pp));;

    return group;
  }

  private Stream<MParameterPair> susceptibleParameterPairs(IMethod method) {
    List<ILocalVariable> parameters;

    try {
      parameters = List.of(method.getParameters());
    } catch (JavaModelException e) {
      e.printStackTrace();

      parameters = List.of();
    }

    var parameterTypeBindingResolver = Dependencies.getParameterTypeBindingResolver();
    var validator = Dependencies.getParameterValidator();

    var validParameters = parameters.stream()
        .filter(parameter -> validator.test(parameter))
        .toList();

    var paramsCount = validParameters.size();

    var factory = Factory.getInstance();

    var mParameterPairs = new ArrayList<MParameterPair>();

    for (var i = 0; i < paramsCount; i++) {
      for (var j = i + 1; j < paramsCount; j++) {
        var b1 = parameterTypeBindingResolver.resolve(validParameters.get(i)).get();
        var b2 = parameterTypeBindingResolver.resolve(validParameters.get(j)).get();
        if (b1.getJavaElement() != null && b2.getJavaElement() != null
            && !b1.getJavaElement().equals(b2.getJavaElement())) {
          var pair = Pair.with(validParameters.get(i), validParameters.get(j));
          mParameterPairs.add(factory.createMParameterPair(pair));
        }
      }
    }

    return mParameterPairs.stream();
  }

}

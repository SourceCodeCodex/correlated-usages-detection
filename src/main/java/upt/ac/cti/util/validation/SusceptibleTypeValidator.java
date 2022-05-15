package upt.ac.cti.util.validation;

import java.util.function.Predicate;
import java.util.stream.Stream;
import org.eclipse.jdt.core.IType;
import familypolymorphismdetection.metamodel.factory.Factory;

public class SusceptibleTypeValidator implements Predicate<IType> {

  @Override
  public boolean test(IType type) {
    try {
      if (type.getTypeParameters().length == 0) {
        var atLeast2Fields = type.getFields().length >= 2;
        var atLeast1Method2Params = Stream.of(type.getMethods())
            .anyMatch(m -> m.getNumberOfParameters() >= 2);

        if (atLeast2Fields || atLeast1Method2Params) {
          var mClass = Factory.getInstance().createMClass(type);
          var atLeast1FieldPair = mClass.susceptibleFieldPairs().getElements().size() != 0;
          var atLeast1ParameterPair = mClass.susceptibleParameterPairs().getElements().size() != 0;
          if (atLeast1FieldPair || atLeast1ParameterPair) {
            return true;
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}

package upt.se.utils.crawlers.hierarchy;

import static upt.se.utils.builders.ListBuilder.toList;
import static upt.se.utils.crawlers.hierarchy.AllSubtypes.getAllSubtypes;
import static upt.se.utils.helpers.ClassNames.isEqual;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.Tuple;
import io.vavr.control.Try;
import thesis.metamodel.entity.MArgumentType;

public class InheritanceArgumentTypes {

  public static List<ITypeBinding> usagesInInheritance(MArgumentType entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .map(type -> type.getDeclaringClass())
        .map(type -> getAllSubtypes(type))
        .map(usages -> toList(usages).asJava())
        .map(usages -> getTypeArguments(usages, entity.getUnderlyingObject()))
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(Collections.emptyList()))
        .get();
  }

  public static List<ITypeBinding> getTypeArguments(List<ITypeBinding> declaringClasses,
      ITypeBinding parameter) {
    return Tuple.of(getParameterNumber(parameter),
        toList(declaringClasses).map(declaringClass -> declaringClass.getSuperclass())
            .map(superClass -> superClass.getTypeArguments())
            .map(typeArguments -> toList(typeArguments)))
        .map((parameterPos, declaringClass) -> Tuple.of(parameterPos,
            declaringClass.map(typeArguments -> typeArguments.get(parameterPos))))
        ._2()
        .asJava();
  }

  private static int getParameterNumber(ITypeBinding actualParameter) {
    return toList(actualParameter.getDeclaringClass().getTypeParameters())
        .zipWithIndex()
        .filter(parameter -> isEqual(parameter._1, actualParameter))
        .map(parameter -> parameter._2)
        .head();
  }
}

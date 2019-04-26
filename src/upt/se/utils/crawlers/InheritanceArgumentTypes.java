package upt.se.utils.crawlers;

import static upt.se.utils.helpers.Converter.convert;
import static upt.se.utils.helpers.Equals.isEqual;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.logging.Level;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import thesis.metamodel.entity.MArgumentType;

public class InheritanceArgumentTypes {

  public static List<ITypeBinding> getAllSubtypes(ITypeBinding typeBinding) {
    return Try.of(() -> convert(typeBinding))
        .flatMap(Option::toTry)
        .mapTry(type -> type.newTypeHierarchy(new NullProgressMonitor())
            .getAllSubtypes(type))
        .map(List::of)
        .flatMap(subTypes -> convert(subTypes).toTry())
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(List.empty()))
        .get();
  }

  public static List<ITypeBinding> getUsages(MArgumentType entity) {
    return Try.of(() -> entity.getUnderlyingObject())
        .map(type -> type.getDeclaringClass())
        .map(type -> getAllSubtypes(type))
        .map(usages -> List.ofAll(usages))
        .map(usages -> getTypeArguments(usages, entity.getUnderlyingObject()))
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(List.empty()))
        .get();
  }

  private static List<ITypeBinding> getTypeArguments(List<ITypeBinding> declaringClasses,
      ITypeBinding parameter) {
    return Tuple.of(getParameterNumber(parameter),
        List.ofAll(declaringClasses).map(declaringClass -> declaringClass.getSuperclass())
            .map(superClass -> superClass.getTypeArguments())
            .map(typeArguments -> List.of(typeArguments)))
        .map((parameterPos, declaringClass) -> Tuple.of(parameterPos,
            declaringClass.map(typeArguments -> typeArguments.get(parameterPos))))
        ._2();
  }

  private static int getParameterNumber(ITypeBinding actualParameter) {
    return List.of(actualParameter.getDeclaringClass().getTypeParameters())
        .zipWithIndex()
        .filter(parameter -> isEqual(parameter._1, actualParameter))
        .map(parameter -> parameter._2)
        .head();
  }
}

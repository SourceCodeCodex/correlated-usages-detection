package upt.se.utils.crawlers;

import static upt.se.utils.visitors.IType2ITypeDeclarationBindingConverter.convert;
import static upt.se.utils.helpers.Equals.*;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import static upt.se.utils.helpers.LoggerHelper.NULL_PROGRESS_MONITOR;
import java.util.logging.Level;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.collection.List;
import io.vavr.control.Try;

public class InheritanceArgumentTypes extends Crawler {


  public static List<List<ITypeBinding>> getUsages(ITypeBinding entity) {
    return Try.of(() -> getAllSubtypes(entity))
        .map(usages -> getTypeArguments(usages, entity))
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(List.empty()))
        .get();
  }
  
  public static List<ITypeBinding> getAllSubtypes(ITypeBinding typeBinding) {
    return Try.of(() -> (IType) typeBinding.getJavaElement())
        .mapTry(type -> type.newTypeHierarchy(NULL_PROGRESS_MONITOR)
            .getAllSubtypes(type))
        .map(List::of)
        .flatMap(subTypes -> convert(subTypes).toTry())
        .onFailure(t -> LOGGER.log(Level.SEVERE, "An error has occurred", t))
        .orElse(() -> Try.success(List.empty()))
        .get();
  }

  private static List<List<ITypeBinding>> getTypeArguments(List<ITypeBinding> declaringClasses,
      ITypeBinding parameter) {
    return List.ofAll(declaringClasses)
            .map(declaringClass -> getSuperclass(declaringClass, parameter.getDeclaringClass()))
            .map(superClass -> superClass.getTypeArguments())
            .map(typeArguments -> List.of(typeArguments));
  }

  private static ITypeBinding getSuperclass(ITypeBinding declaringClass,
      ITypeBinding searchedClass) {
    return Try.of(() -> declaringClass)
        .filter(unit -> isObject(declaringClass.getSuperclass())
            && isEqual(declaringClass.getSuperclass().getErasure(), searchedClass.getErasure()))
        .fold(notEqual -> List.of(declaringClass.getInterfaces())
            .filter(interfaceType -> isEqual(interfaceType.getErasure(), searchedClass))
            .headOption()
            .fold(() -> declaringClass.getSuperclass(), foundInterface -> foundInterface),
            unit -> declaringClass.getSuperclass());
  }
}

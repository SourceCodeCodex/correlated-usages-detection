package upt.se.arguments.single;

import static java.util.function.Function.identity;
import static upt.se.utils.helpers.Converter.round;
import static upt.se.utils.helpers.LoggerHelper.NULL_PROGRESS_MONITOR;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MParameter;

@PropertyComputer
public class Aperture implements IPropertyComputer<Double, MParameter> {

  @Override
  public Double compute(MParameter entity) {
    int usedTypesCount = List.ofAll(entity.usedArgumentTypes().getElements())
        .map(type -> type.getUnderlyingObject())
        .map(type -> Try.of(() -> type.newTypeHierarchy(NULL_PROGRESS_MONITOR))
            .map(hierarchy -> hierarchy.getAllSubtypes(type))
            .map(List::of)
            .orElse(Try.success(List.empty()))
            .get().prepend(type))
        .flatMap(identity())
        .distinctBy(type -> type.getFullyQualifiedName())
        .size();

    double apperture = usedTypesCount * 100d
        / entity.allPossibleArgumentTypes().getElements().size();

    return round(apperture, 2);
  }

}

package upt.se.arguments.pair;

import static upt.se.utils.helpers.Converter.round;
import static upt.se.utils.helpers.LoggerHelper.NULL_PROGRESS_MONITOR;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MParameterPair;

@PropertyComputer
public class Aperture implements IPropertyComputer<Double, MParameterPair> {

  @Override
  public Double compute(MParameterPair entity) {
    int usedTypesCount = List.ofAll(entity.usedArgumentsTypes().getElements())
        .map(pair -> pair.getUnderlyingObject())
        .map(pair -> pair.toTuple())
        .map(tuple -> tuple.map(type -> Try.of(() -> type.newTypeHierarchy(NULL_PROGRESS_MONITOR))
            .map(hierarchy -> List.of(hierarchy.getAllSubtypes(type)))
            .orElse(Try.success(List.empty()))
            .get().prepend(type),
            type -> Try.of(() -> type.newTypeHierarchy(NULL_PROGRESS_MONITOR))
                .map(hierarchy -> List.of(hierarchy.getAllSubtypes(type)))
                .orElse(Try.success(List.empty()))
                .get().prepend(type)))
        .flatMap(tuple -> tuple._1.crossProduct(tuple._2))
        .distinctBy(
            pair -> Try.of(() -> pair._1.getFullyQualifiedParameterizedName()).getOrElse("") + "," +
                Try.of(() -> pair._2.getFullyQualifiedParameterizedName()).getOrElse(""))
        .size();

    double apperture = usedTypesCount * 100d
        / entity.allPossibleArgumentsTypes().getElements().size();

    return round(apperture, 2);
  }

}

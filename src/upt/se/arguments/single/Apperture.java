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
public class Apperture implements IPropertyComputer<String, MParameter> {

  @Override
  public String compute(MParameter entity) {
    int usedTypesCount = Try.sequence(List.ofAll(entity.usedArgumentTypes().getElements())
        .map(type -> type.getUnderlyingObject())
        .map(type -> Try.of(() -> type.newTypeHierarchy(NULL_PROGRESS_MONITOR))
            .map(hierarchy -> hierarchy.getAllSubtypes(type))
            .map(List::of)
            .map(types -> types.append(type))))
        .map(types -> types.flatMap(identity()))
        .map(types -> types.distinctBy(type -> type.getFullyQualifiedName()))
        .map(types -> types.size())
        .orElse(Try.success(0))
        .get();

    double apperture = usedTypesCount * 100d
        / entity.allPossibleArgumentTypes().getElements().size();

    return round(apperture, 2) + " %";
  }

}

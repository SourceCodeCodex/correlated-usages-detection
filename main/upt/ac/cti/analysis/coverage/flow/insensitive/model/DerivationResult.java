package upt.ac.cti.analysis.coverage.flow.insensitive.model;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;

public sealed interface DerivationResult
permits Car, Truck

public record DerivationResult(
    List<Pair<FieldWriting, FieldWriting>> newPairs,
    Optional<Pair<ITypeBinding, ITypeBinding>> resolvedCorelation) {

    public static DerivationResult NULL = new DerivationResult(List.of(), Optional.empty());

}

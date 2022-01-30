package upt.ac.cti.coverage.analysis.iterative.model;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;

public record CPHandlingResult(
    List<CorelationPair> newPairs,
    Optional<Pair<ITypeBinding, ITypeBinding>> resolvedCorelation) {

}

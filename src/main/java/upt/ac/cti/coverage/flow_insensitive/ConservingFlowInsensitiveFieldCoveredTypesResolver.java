package upt.ac.cti.coverage.flow_insensitive;

import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.cache.CacheRegion;

public final class ConservingFlowInsensitiveFieldCoveredTypesResolver
    extends AFlowInsensitiveCoveredTypesResolver<IField> {

  public ConservingFlowInsensitiveFieldCoveredTypesResolver() {
    super(Dependencies.fieldWritingsCombiner,
        Dependencies.fieldConservingDerivationManager,
        CacheRegion.COVERED_TYPES_CFI);
  }

  @Override
  public Optional<Set<Pair<IType, IType>>> resolve(IField field1, IField field2) {
    var r = super.resolve(field1, field2);
    if (r.isEmpty()) {
      return r;
    }

    var s = r.get();
    if (s.isEmpty()) {
      return Optional.of(Dependencies.fieldAllTypePairsResolver.resolve(field1, field2));
    }
    return r;
  }

}

package upt.ac.cti.coverage.flow_insensitive;

import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.cache.CacheRegion;

public final class SquanderingFlowInsensitiveFieldCoveredTypesResolver
    extends AFlowInsensitiveCoveredTypesResolver<IField> {

  public SquanderingFlowInsensitiveFieldCoveredTypesResolver() {
    super(Dependencies.fieldWritingsCombiner,
        Dependencies.fieldSquanderingDerivationManager,
        CacheRegion.COVERED_TYPES_SFI);
  }

  @Override
  public Optional<Set<Pair<IType, IType>>> resolve(IField field1, IField field2) {
    return super.resolve(field1, field2);
  }

}

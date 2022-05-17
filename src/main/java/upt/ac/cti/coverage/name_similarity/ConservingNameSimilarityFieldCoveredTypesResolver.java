package upt.ac.cti.coverage.name_similarity;

import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.dependency.Dependencies;

public final class ConservingNameSimilarityFieldCoveredTypesResolver
    extends AConservingNameSimilarityCoveredTypesResolver<IField> {

  public ConservingNameSimilarityFieldCoveredTypesResolver() {
    super(Dependencies.fieldTypeBindingResolver, Dependencies.fieldAllTypePairsResolver);
  }

  @Override
  public Optional<Set<Pair<IType, IType>>> resolve(IField field1, IField field2) {
    return super.resolve(field1, field2);
  }

}

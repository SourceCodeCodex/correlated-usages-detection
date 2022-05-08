package upt.ac.cti.coverage;

import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.aperture.FieldAllTypePairsResolver;
import upt.ac.cti.coverage.combiner.field.FieldWritingsCombiner;
import upt.ac.cti.util.binding.FieldTypeBindingResolver;
import upt.ac.cti.util.hierarchy.HierarchyResolver;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public final class FieldCoveredTypesResolver extends ACoveredTypesResolver<IField> {

  public FieldCoveredTypesResolver(
      CodeParser codeParser,
      JavaEntitySearcher javaEntitySearcher,
      FieldTypeBindingResolver fieldTypeBindingResolver,
      HierarchyResolver hierarchyResolver,
      FieldAllTypePairsResolver fieldAllTypePairsResolver) {
    super(new FieldWritingsCombiner(codeParser, javaEntitySearcher),
        codeParser,
        javaEntitySearcher,
        fieldTypeBindingResolver,
        fieldAllTypePairsResolver);
  }

  @Override
  public Optional<Set<Pair<IType, IType>>> resolve(IField field1, IField field2) {
    return super.resolve(field1, field2);
  }

}

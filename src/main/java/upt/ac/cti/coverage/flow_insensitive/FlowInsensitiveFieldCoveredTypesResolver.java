package upt.ac.cti.coverage.flow_insensitive;

import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.aperture.FieldAllTypePairsResolver;
import upt.ac.cti.coverage.flow_insensitive.combiner.field.FieldWritingsCombiner;
import upt.ac.cti.coverage.flow_insensitive.derivator.util.FieldWritingBindingResolver;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public final class FlowInsensitiveFieldCoveredTypesResolver extends AFlowInsensitiveCoveredTypesResolver<IField> {

  public FlowInsensitiveFieldCoveredTypesResolver(
      CodeParser codeParser,
      JavaEntitySearcher javaEntitySearcher,
      FieldWritingBindingResolver fieldWritingBindingResolver,
      FieldAllTypePairsResolver fieldAllTypePairsResolver) {
    super(new FieldWritingsCombiner(codeParser, javaEntitySearcher),
        codeParser,
        javaEntitySearcher,
        fieldAllTypePairsResolver,
        fieldWritingBindingResolver);
  }

  @Override
  public Optional<Set<Pair<IType, IType>>> resolve(IField field1, IField field2) {
    return super.resolve(field1, field2);
  }

}

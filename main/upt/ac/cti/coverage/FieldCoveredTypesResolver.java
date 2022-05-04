package upt.ac.cti.coverage;

import java.util.Set;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.coverage.combiner.field.FieldWritingsCombiner;
import upt.ac.cti.coverage.parsing.CodeParser;
import upt.ac.cti.coverage.search.JavaEntitySearcher;

public final class FieldCoveredTypesResolver extends ACoveredTypesResolver<IField> {

  public FieldCoveredTypesResolver(CodeParser codeParser, JavaEntitySearcher javaEntitySearcher) {
    super(new FieldWritingsCombiner(codeParser, javaEntitySearcher), codeParser,
        javaEntitySearcher);
  }

  @Override
  public Set<Pair<IType, IType>> resolve(IField field1, IField field2) {
    return super.resolve(field1, field2);
  }

}

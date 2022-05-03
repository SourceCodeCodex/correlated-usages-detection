package upt.ac.cti.coverage;

import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import upt.ac.cti.coverage.combiner.field.FieldWritingsCombiner;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.parsing.CodeParser;
import upt.ac.cti.coverage.search.JavaEntitySearcher;

public final class FieldCoveredTypesResolver extends ACoveredTypesResolver {

  public FieldCoveredTypesResolver(CodeParser codeParser, JavaEntitySearcher javaEntitySearcher) {
    super(codeParser, javaEntitySearcher);
  }

  public Set<Pair<IType, IType>> resolve(IField field1, IField field2) {
    return super.resolve(field1, field2);
  }

  @Override
  protected List<Pair<Writing, Writing>> writingPairs(IJavaElement javaElement1,
      IJavaElement javaElement2) {

    var combiner = new FieldWritingsCombiner(codeParser, javaEntitySearcher);

    return combiner.combine((IField) javaElement1, (IField) javaElement2);
  }

}

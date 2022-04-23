package upt.ac.cti.analysis.coverage.flow.insensitive;

import java.util.List;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import upt.ac.cti.analysis.coverage.ICoverageAnalysis;
import upt.ac.cti.analysis.coverage.flow.insensitive.combiner.FieldWritingsCombiner;
import upt.ac.cti.analysis.coverage.flow.insensitive.derivator.DerivationManager;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.JavaEntitySearcher;
import upt.ac.cti.analysis.coverage.flow.insensitive.util.FieldWritingBindingResolver;
import upt.ac.cti.util.HierarchyResolver;

public final class CoverageAnalysis implements ICoverageAnalysis {

  private final IField field1;
  private final IField field2;

  public CoverageAnalysis(MFieldPair mFieldPair) {
    @SuppressWarnings("unchecked")
    var pair = (Pair<IField, IField>) mFieldPair.getUnderlyingObject();

    this.field1 = pair.getValue0();
    this.field2 = pair.getValue1();
  }


  @Override
  public List<Pair<IType, IType>> coveredTypes() {
    var codeParser = new CodeParser();
    var javaEntitySearcher = new JavaEntitySearcher();

    var fieldWritingsCombiner = new FieldWritingsCombiner(codeParser, javaEntitySearcher);

    var writingPairs = fieldWritingsCombiner.combine(field1, field2);

    var hierarchyResolver = new HierarchyResolver();
    var assignmentBindingResolver = new FieldWritingBindingResolver(hierarchyResolver);

    var deriver = new DerivationManager(assignmentBindingResolver, javaEntitySearcher, codeParser);

    var bindingPairs = deriver.derive(writingPairs);

    var coveredTypes = bindingPairs.stream().map(p -> Pair
        .with((IType) p.getValue0().getJavaElement(), (IType) p.getValue1().getJavaElement()))
        .toList();

    return coveredTypes;
  }

}

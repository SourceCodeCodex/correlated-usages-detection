package upt.ac.cti.coverage.analysis.flow.insensitive.generators;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import upt.ac.cti.coverage.analysis.flow.insensitive.generators.visitors.FieldAsgmtVisitor;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CorelationPair;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.FieldAsgmt;
import upt.ac.cti.utils.parsers.CachedParser;
import upt.ac.cti.utils.searchers.WritesSearcher;

public class MethodBasedCPGenerator implements CPGenerator {

  private final IField field1;
  private final IField field2;

  public MethodBasedCPGenerator(IField field1, IField field2) {
    this.field1 = field1;
    this.field2 = field2;
  }

  @Override
  public List<CorelationPair> generate() {

    var f1AssignmentsMethods = WritesSearcher.instance().searchFieldWrites(field1);
    var f2AssignmentsMethods = WritesSearcher.instance().searchFieldWrites(field2);

    var writingBoth = new HashSet<IMethod>(f1AssignmentsMethods);
    writingBoth.retainAll(f2AssignmentsMethods);

    var result = new LinkedList<CorelationPair>();

    // Combining only writes in the context of a method writing both fields
    writingBoth.forEach(it -> {
      var f1Asgmts = computeFieldAssignments(it, field1).stream().toList();
      var f2Asgmts = computeFieldAssignments(it, field2).stream().toList();

      for (FieldAsgmt f1Asgmt : f1Asgmts) {
        for (FieldAsgmt f2Asgmt : f2Asgmts) {
          result.add(new CorelationPair(f1Asgmt, f2Asgmt, Optional.empty(), Optional.empty()));
        }
      }
    });

    // Combining writes that are in a different method and write only one variable
    f1AssignmentsMethods.removeAll(writingBoth);
    f2AssignmentsMethods.removeAll(writingBoth);

    var f1Asgmts = f1AssignmentsMethods.stream()
        .flatMap(it -> computeFieldAssignments(it, field1).stream()).toList();

    var f2Asgmts = f2AssignmentsMethods.stream()
        .flatMap(it -> computeFieldAssignments(it, field2).stream()).toList();


    for (FieldAsgmt f1Asgmt : f1Asgmts) {
      for (FieldAsgmt f2Asgmt : f2Asgmts) {
        result.add(new CorelationPair(f1Asgmt, f2Asgmt, Optional.empty(), Optional.empty()));
      }
    }

    return result;
  }

  private List<FieldAsgmt> computeFieldAssignments(IMethod iMethod, IField iField) {
    var visitor = new FieldAsgmtVisitor(iField);
    var node = CachedParser.instance().parse(iMethod).getBody();
    node.accept(visitor);
    return visitor.result();
  }

}

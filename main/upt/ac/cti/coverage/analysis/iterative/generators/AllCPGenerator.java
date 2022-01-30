package upt.ac.cti.coverage.analysis.iterative.generators;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.IField;
import upt.ac.cti.coverage.analysis.iterative.model.CorelationPair;
import upt.ac.cti.coverage.analysis.iterative.model.FieldAsgmt;
import upt.ac.cti.coverage.analysis.iterative.visitors.AsgmtVisitor;
import upt.ac.cti.utils.parsers.CachedParser;
import upt.ac.cti.utils.searchers.WritesSearcher;

public class AllCPGenerator implements CPGenerator {
  private final IField field1;
  private final IField field2;

  public AllCPGenerator(IField field1, IField field2) {
    this.field1 = field1;
    this.field2 = field2;
  }

  @Override
  public List<CorelationPair> generate() {

    var f1AssignmentsMethods = WritesSearcher.instance().searchFieldWrites(field1);
    var f2AssignmentsMethods = WritesSearcher.instance().searchFieldWrites(field2);

    var f1Asgmts = f1AssignmentsMethods.stream().flatMap(it -> {
      var visitor = new AsgmtVisitor(field1, field1, it);
      var node = CachedParser.instance().parse(it).getBody();
      node.accept(visitor);
      return visitor.result().stream();
    }).toList();

    var f2Asgmts = f2AssignmentsMethods.stream().flatMap(it -> {
      var visitor = new AsgmtVisitor(field2, field2, it);
      var node = CachedParser.instance().parse(it).getBody();
      node.accept(visitor);
      return visitor.result().stream();
    }).toList();

    var result = new LinkedList<CorelationPair>();

    for (FieldAsgmt f1Asgmt : f1Asgmts) {
      for (FieldAsgmt f2Asgmt : f2Asgmts) {
        result.add(new CorelationPair(f1Asgmt, f2Asgmt, Optional.empty(), Optional.empty()));
      }
    }

    return result;
  }
}

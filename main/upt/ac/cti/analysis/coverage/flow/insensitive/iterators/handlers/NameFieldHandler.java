package upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers;

import java.util.Optional;
import org.eclipse.jdt.core.IField;
import upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers.visitors.FieldAsgmtVisitor;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.WritingsSearcher;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CPIndex;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CorelationPair;

final class NameFieldHandler extends RightSideHandler {

  private final IField iField;

  public NameFieldHandler(CorelationPair cp, CPIndex index, IField iField) {
    super(cp, index);
    this.iField = iField;
  }

  @Override
  public DerivationResult handle() {
    var assignmentMethods = WritingsSearcher.instance().searchFieldWrites(iField);

    var rightExpressions = assignmentMethods.stream().flatMap(it -> {
      var visitor = new FieldAsgmtVisitor(iField);
      var node = CodeParser.instance().parse(it).getBody();
      node.accept(visitor);
      return visitor.result().stream();
    }).toList();

    var newPairs = rightExpressions.stream()
        .map(expr -> {
          var updatedRight = cp.fieldAsgmt(index).withExpression(expr);
          return cp.withFieldAsgmt(updatedRight, index);
        })
        .toList();;

    return new DerivationResult(newPairs, Optional.empty());
  }


}


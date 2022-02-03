package upt.ac.cti.coverage.analysis.flow.insensitive.iterators.handlers;

import java.util.Optional;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import upt.ac.cti.coverage.analysis.flow.insensitive.iterators.handlers.visitors.FieldAsgmtVisitor;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPHandlingResult;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPIndex;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CorelationPair;
import upt.ac.cti.utils.parsers.CachedParser;
import upt.ac.cti.utils.searchers.WritesSearcher;

final class SuperFieldAccessHandler extends RightSideHandler {

  public SuperFieldAccessHandler(CorelationPair cp, CPIndex index) {
    super(cp, index);
  }

  @Override
  public CPHandlingResult handle() {
    var invoc = (SuperFieldAccess) cp.fieldAsgmt(index).rightSide();
    var iField = (IField) invoc.resolveFieldBinding().getJavaElement();

    var assignmentMethods = WritesSearcher.instance().searchFieldWrites(iField);

    var rightExpressions = assignmentMethods.stream().flatMap(it -> {
      var visitor = new FieldAsgmtVisitor(iField);
      var node = CachedParser.instance().parse(it).getBody();
      node.accept(visitor);
      return visitor.result().stream();
    }).toList();

    var newPairs = rightExpressions.stream()
        .map(expr -> {
          var updatedRight = cp.fieldAsgmt(index).withRightSide(expr);
          return cp.withFieldAsgmt(updatedRight, index);
        })
        .toList();;

    return new CPHandlingResult(newPairs, Optional.empty());
  }
}


package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.FieldAccess;
import upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.algorithm.FieldDerivatorAlgorithm;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.JavaEntitySearcher;

final class FieldAccessDerivator implements IFieldWritingsDerivator {

  private final FieldDerivatorAlgorithm algorithm;

  public FieldAccessDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.algorithm = new FieldDerivatorAlgorithm(javaEntitySearcher, codeParser);
  }


  @Override
  public DerivationResult derive(FieldWriting deriver, FieldWriting constant) {
    var fieldAccess = (FieldAccess) deriver.writingExpression();
    var field = (IField) fieldAccess.resolveFieldBinding().getJavaElement();

    return algorithm.derive(deriver, constant, field);
  }

}


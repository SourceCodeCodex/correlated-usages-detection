package upt.ac.cti.coverage.derivator.internal;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import upt.ac.cti.coverage.derivator.internal.algorithm.FieldDerivatorAlgorithm;
import upt.ac.cti.coverage.model.DerivationResult;
import upt.ac.cti.coverage.model.NewWritingPairs;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.parsing.CodeParser;
import upt.ac.cti.coverage.search.JavaEntitySearcher;

final class SuperFieldAccessDerivator implements IFieldWritingsDerivator {

  private final FieldDerivatorAlgorithm algorithm;

  public SuperFieldAccessDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.algorithm = new FieldDerivatorAlgorithm(javaEntitySearcher, codeParser);
  }

  @Override
  public DerivationResult derive(Writing deriver, Writing constant) {
    var fieldAccess = (SuperFieldAccess) deriver.writingExpression();
    var binding = fieldAccess.resolveFieldBinding();
    if (binding == null) {
      return NewWritingPairs.NULL;
    }
    var field = (IField) binding.getJavaElement();
    if (field == null) {
      return NewWritingPairs.NULL;
    }

    return algorithm.derive(deriver, constant, field);
  }

}


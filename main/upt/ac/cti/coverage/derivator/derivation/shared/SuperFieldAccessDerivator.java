package upt.ac.cti.coverage.derivator.derivation.shared;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import upt.ac.cti.coverage.derivator.derivation.IWritingsDerivator;
import upt.ac.cti.coverage.derivator.derivation.shared.algorithm.FieldDerivatorAlgorithm;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public final class SuperFieldAccessDerivator<J extends IJavaElement>
    implements IWritingsDerivator<J> {

  private final FieldDerivatorAlgorithm<J> algorithm;

  public SuperFieldAccessDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.algorithm = new FieldDerivatorAlgorithm<>(javaEntitySearcher, codeParser);
  }

  @Override
  public NewWritingPairs<J> derive(Writing<J> deriver, Writing<J> constant) {
    var fieldAccess = (SuperFieldAccess) deriver.writingExpression();
    var binding = fieldAccess.resolveFieldBinding();
    if (binding == null) {
      return NewWritingPairs.NULL();
    }
    var field = (IField) binding.getJavaElement();
    if (field == null) {
      return NewWritingPairs.NULL();
    }

    return algorithm.derive(deriver, constant, field);
  }

}


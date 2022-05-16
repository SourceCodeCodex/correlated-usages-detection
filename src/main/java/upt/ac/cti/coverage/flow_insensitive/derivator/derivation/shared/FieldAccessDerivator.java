package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.FieldAccess;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.IWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.algorithm.FieldDerivatorAlgorithm;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public final class FieldAccessDerivator<J extends IJavaElement> implements IWritingsDerivator<J> {

  private final FieldDerivatorAlgorithm<J> algorithm;

  public FieldAccessDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.algorithm = new FieldDerivatorAlgorithm<>(javaEntitySearcher, codeParser);
  }


  @Override
  public NewWritingPairs<J> derive(Writing<J> deriver, Writing<J> constant) {
    var fieldAccess = (FieldAccess) deriver.writingExpression();
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


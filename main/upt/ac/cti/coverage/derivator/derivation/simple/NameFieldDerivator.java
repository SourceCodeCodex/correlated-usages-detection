package upt.ac.cti.coverage.derivator.derivation.simple;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.Name;
import upt.ac.cti.coverage.derivator.derivation.IWritingsDerivator;
import upt.ac.cti.coverage.derivator.derivation.simple.algorithm.FieldDerivatorAlgorithm;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

final class NameFieldDerivator<J extends IJavaElement> implements IWritingsDerivator<J> {

  private final FieldDerivatorAlgorithm<J> algorithm;

  public NameFieldDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.algorithm = new FieldDerivatorAlgorithm<>(javaEntitySearcher, codeParser);
  }

  @Override
  public NewWritingPairs<J> derive(Writing<J> deriver, Writing<J> constant) {
    var name = (Name) deriver.writingExpression();
    var binding = name.resolveBinding();
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


package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.Name;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.ISimpleWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.algorithm.FieldDerivatorAlgorithm;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;

public final class NameFieldDerivator<J extends IJavaElement> implements ISimpleWritingsDerivator<J> {

  private final FieldDerivatorAlgorithm<J> algorithm = new FieldDerivatorAlgorithm<>();

  @Override
  public NewWritingPairs<J> derive(DerivableWriting<J> deriver, Writing<J> constant) {
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


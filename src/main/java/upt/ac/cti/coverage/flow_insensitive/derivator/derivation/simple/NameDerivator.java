package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.simple;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.ISimpleWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.NameFieldDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.NameParameterDerivator;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;

final class NameDerivator<J extends IJavaElement> implements ISimpleWritingsDerivator<J> {

  private final NameParameterDerivator<J> p = new NameParameterDerivator<>();
  private final NameFieldDerivator<J> f = new NameFieldDerivator<>();
  private final NameLocalVariableDerivator<J> lv = new NameLocalVariableDerivator<>();

  @Override
  public NewWritingPairs<J> derive(DerivableWriting<J> deriver, Writing<J> constant) {
    var name = (Name) deriver.writingExpression();
    var binding = name.resolveBinding();

    var varBinding = (IVariableBinding) binding;

    if (varBinding.isParameter()) {
      return p.derive(deriver, constant);
    }

    if (varBinding.isField()) {
      return f.derive(deriver, constant);
    }

    return lv.derive(deriver, constant);
  }

}



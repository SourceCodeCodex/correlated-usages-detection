package upt.ac.cti.coverage.derivator.derivation.simple;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import upt.ac.cti.coverage.derivator.derivation.IWritingsDerivator;
import upt.ac.cti.coverage.derivator.derivation.shared.NameFieldDerivator;
import upt.ac.cti.coverage.derivator.derivation.shared.NameParameterDerivator;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

final class NameDerivator<J extends IJavaElement> implements IWritingsDerivator<J> {

  private final NameParameterDerivator<J> p;
  private final NameFieldDerivator<J> f;
  private final NameLocalVariableDerivator<J> lv;


  public NameDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.p = new NameParameterDerivator<>(javaEntitySearcher, codeParser);
    this.f = new NameFieldDerivator<>(javaEntitySearcher, codeParser);
    this.lv = new NameLocalVariableDerivator<>(javaEntitySearcher, codeParser);
  }

  @Override
  public NewWritingPairs<J> derive(Writing<J> deriver, Writing<J> constant) {
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



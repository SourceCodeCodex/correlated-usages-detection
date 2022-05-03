package upt.ac.cti.coverage.derivator.internal;

import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import upt.ac.cti.coverage.model.DerivationResult;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.parsing.CodeParser;
import upt.ac.cti.coverage.search.JavaEntitySearcher;

final class NameDerivator implements IFieldWritingsDerivator {

  private final NameParameterDerivator p;
  private final NameFieldDerivator f;
  private final NameLocalVariableDerivator lv;


  public NameDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.p = new NameParameterDerivator(javaEntitySearcher, codeParser);
    this.f = new NameFieldDerivator(javaEntitySearcher, codeParser);
    this.lv = new NameLocalVariableDerivator(javaEntitySearcher, codeParser);
  }

  @Override
  public DerivationResult derive(Writing deriver, Writing constant) {
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



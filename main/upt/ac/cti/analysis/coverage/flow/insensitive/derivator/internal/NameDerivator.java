package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal;

import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.JavaEntitySearcher;

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
  public DerivationResult derive(FieldWriting deriver, FieldWriting constant) {
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



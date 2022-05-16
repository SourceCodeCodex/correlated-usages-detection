package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.MethodInvocationDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.SuperMethodInvocationDerivator;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;
import upt.ac.cti.util.parsing.CodeParser;

class MDerivator<J extends IJavaElement> implements IEntityDerivator<J> {

  private final MethodInvocationDerivator<J> mi;
  private final SuperMethodInvocationDerivator<J> smi;

  public MDerivator(CodeParser codeParser) {
    mi = new MethodInvocationDerivator<>(codeParser);
    smi = new SuperMethodInvocationDerivator<>(codeParser);
  }

  @Override
  public NewWritingPairs<J> derive(Writing<J> w) {
    var writeType = w.writingExpression().getNodeType();
    if (writeType == ASTNode.METHOD_INVOCATION) {
      return mi.derive(w, null);
    }
    return smi.derive(w, null);
  }

}

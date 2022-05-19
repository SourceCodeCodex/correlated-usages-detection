package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.FieldAccessDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.NameFieldDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.SuperFieldAccessDerivator;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;

class FDerivator<J extends IJavaElement> implements IEntityDerivator<J> {

  private final NameFieldDerivator<J> nf = new NameFieldDerivator<>();
  private final FieldAccessDerivator<J> fa = new FieldAccessDerivator<>();
  private final SuperFieldAccessDerivator<J> sfa = new SuperFieldAccessDerivator<>();

  @Override
  public NewWritingPairs<J> derive(DerivableWriting<J> w) {
    var writeType = w.writingExpression().getNodeType();
    if (writeType == ASTNode.FIELD_ACCESS) {
      return fa.derive(w, null);
    }
    if (writeType == ASTNode.SUPER_FIELD_ACCESS) {
      return sfa.derive(w, null);
    }
    return nf.derive(w, null);
  }

}

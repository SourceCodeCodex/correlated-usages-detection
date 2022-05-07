package upt.ac.cti.coverage.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import upt.ac.cti.coverage.derivator.derivation.shared.FieldAccessDerivator;
import upt.ac.cti.coverage.derivator.derivation.shared.NameFieldDerivator;
import upt.ac.cti.coverage.derivator.derivation.shared.SuperFieldAccessDerivator;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

class FDerivator<J extends IJavaElement> implements IEntityDerivator<J> {

  private final NameFieldDerivator<J> nf;
  private final FieldAccessDerivator<J> fa;
  private final SuperFieldAccessDerivator<J> sfa;

  public FDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    nf = new NameFieldDerivator<>(javaEntitySearcher, codeParser);
    fa = new FieldAccessDerivator<>(javaEntitySearcher, codeParser);
    sfa = new SuperFieldAccessDerivator<>(javaEntitySearcher, codeParser);
  }

  @Override
  public NewWritingPairs<J> derive(Writing<J> w) {
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

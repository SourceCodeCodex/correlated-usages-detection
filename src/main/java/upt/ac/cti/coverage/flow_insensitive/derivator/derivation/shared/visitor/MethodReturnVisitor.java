package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.visitor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;

public class MethodReturnVisitor<J extends IJavaElement> extends ASTVisitor {

  private final DerivableWriting<J> deriver;

  private final List<DerivableWriting<J>> derivations = new ArrayList<>();

  public MethodReturnVisitor(DerivableWriting<J> deriver) {
    this.deriver = deriver;
  }

  public List<DerivableWriting<J>> derivations() {
    return derivations;
  }

  @Override
  public boolean visit(AnonymousClassDeclaration node) {
    return false;
  }

  @Override
  public boolean visit(ReturnStatement node) {
    if (node.getExpression() != null) {
      derivations.add(deriver.withWritingExpression(node.getExpression()));
    }
    return false;
  }

}

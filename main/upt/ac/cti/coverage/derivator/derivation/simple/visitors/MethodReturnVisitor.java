package upt.ac.cti.coverage.derivator.derivation.simple.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ReturnStatement;
import upt.ac.cti.coverage.model.Writing;

public class MethodReturnVisitor<J extends IJavaElement> extends ASTVisitor {

  private final Writing<J> deriver;

  private final List<Writing<J>> derivations = new ArrayList<>();

  public MethodReturnVisitor(Writing<J> deriver) {
    this.deriver = deriver;
  }

  public List<Writing<J>> derivations() {
    return derivations;
  }

  @Override
  public boolean visit(ReturnStatement node) {
    derivations.add(deriver.withWritingExpression(node.getExpression()));
    return true;
  }

}

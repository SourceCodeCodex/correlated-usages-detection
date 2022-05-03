package upt.ac.cti.coverage.derivator.internal.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ReturnStatement;
import upt.ac.cti.coverage.model.Writing;

public class MethodReturnVisitor extends ASTVisitor {

  private final Writing deriver;

  private final List<Writing> derivations = new ArrayList<>();

  public MethodReturnVisitor(Writing deriver) {
    this.deriver = deriver;
  }

  public List<Writing> derivations() {
    return derivations;
  }

  @Override
  public boolean visit(ReturnStatement node) {
    derivations.add(deriver.withWritingExpression(node.getExpression()));
    return true;
  }

}

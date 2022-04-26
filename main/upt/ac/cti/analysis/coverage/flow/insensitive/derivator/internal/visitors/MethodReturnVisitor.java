package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ReturnStatement;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;

public class MethodReturnVisitor extends ASTVisitor {

  private final FieldWriting deriver;

  private final List<FieldWriting> derivations = new ArrayList<>();

  public MethodReturnVisitor(FieldWriting deriver) {
    this.deriver = deriver;
  }

  public List<FieldWriting> derivations() {
    return derivations;
  }

  @Override
  public boolean visit(ReturnStatement node) {
    derivations.add(deriver.withWritingExpression(node.getExpression()));
    return true;
  }

}

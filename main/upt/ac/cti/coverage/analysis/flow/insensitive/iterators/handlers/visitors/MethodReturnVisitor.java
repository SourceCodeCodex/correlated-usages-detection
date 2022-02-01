package upt.ac.cti.coverage.analysis.flow.insensitive.iterators.handlers.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ReturnStatement;

public class MethodReturnVisitor extends ASTVisitor {

  private final List<Expression> result = new ArrayList<>();

  public List<Expression> result() {
    return result;
  }

  @Override
  public boolean visit(ReturnStatement node) {
    result.add(node.getExpression());
    return true;
  }

}

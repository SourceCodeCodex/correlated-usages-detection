package upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ASTNode;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CPIndex;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CorelationPair;

final public class CPFieldHandler extends RightSideHandler {

  private final FieldAccessHandler fah = new FieldAccessHandler(cp, index);
  private final MethodInvocationHandler mih = new MethodInvocationHandler(cp, index);
  private final SuperFieldAccessHandler sfah = new SuperFieldAccessHandler(cp, index);
  private final SuperMethodInvocationHandler smih = new SuperMethodInvocationHandler(cp, index);
  private final AssignmentHandler ah = new AssignmentHandler(cp, index);
  private final ParenthesizedExpressionHandler peh = new ParenthesizedExpressionHandler(cp, index);
  private final ConditionalExpressionHandler ceh = new ConditionalExpressionHandler(cp, index);
  private final NameHandler nh = new NameHandler(cp, index);


  public CPFieldHandler(CorelationPair cp, CPIndex index) {
    super(cp, index);
  }

  @Override
  public DerivationResult handle() {
    switch (rightExpressionType()) {
      case ASTNode.FIELD_ACCESS: {
        return fah.handle();
      }
      case ASTNode.METHOD_INVOCATION: {
        return mih.handle();
      }
      case ASTNode.SUPER_FIELD_ACCESS: {
        return sfah.handle();
      }
      case ASTNode.SUPER_METHOD_INVOCATION: {
        return smih.handle();
      }
      case ASTNode.ASSIGNMENT: {
        return ah.handle();
      }
      case ASTNode.PARENTHESIZED_EXPRESSION: {
        return peh.handle();
      }
      case ASTNode.CONDITIONAL_EXPRESSION: {
        return ceh.handle();

      }
      case ASTNode.SIMPLE_NAME:
      case ASTNode.QUALIFIED_NAME: {
        return nh.handle();

      }
      default:
        return new DerivationResult(List.of(), Optional.empty());
    }
  }

  private int rightExpressionType() {
    var fieldAsgmt = cp.fieldAsgmt(index);
    return fieldAsgmt.rightSide().getNodeType();
  }
}

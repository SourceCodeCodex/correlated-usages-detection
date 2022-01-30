package upt.ac.cti.coverage.analysis.iterative.iterators.handlers;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ASTNode;
import upt.ac.cti.coverage.analysis.iterative.model.CPHandlingResult;

//@formatter:off
public abstract class CPFieldHandler<
H     extends CPFieldHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
FAHT  extends FieldAccessHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
MIHT  extends MethodInvocationHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
SFAHT extends SuperFieldAccessHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
SMIHT extends SuperMethodInvocationHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
AHT   extends AssignmentHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
PEHT  extends ParenthesizedExpressionHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
CEHT  extends ConditionalExpressionHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
NHT   extends NameHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>
> {
//@formatter:on

  private final int rightExpressionType;
  private final FAHT fieldAccessHandler;
  private final MIHT methodInvocationHandler;
  private final SFAHT superFieldAccessHandler;
  private final SMIHT superMethodInvocationHandler;
  private final AHT assignmentHandler;
  private final PEHT parenthesizedExpressionHandler;
  private final CEHT conditionalExpressionHandler;
  private final NHT nameHandler;


  public CPFieldHandler(
      int rightExpressionType,
      FAHT fieldAccessHandler,
      MIHT methodInvocationHandler,
      SFAHT superFieldAccessHandler,
      SMIHT superMethodInvocationHandler,
      AHT assignmentHandler,
      PEHT parenthesizedExpressionHandler,
      CEHT conditionalExpressionHandler,
      NHT nameHandler) {
    this.rightExpressionType = rightExpressionType;
    this.fieldAccessHandler = fieldAccessHandler;
    this.methodInvocationHandler = methodInvocationHandler;
    this.superFieldAccessHandler = superFieldAccessHandler;
    this.superMethodInvocationHandler = superMethodInvocationHandler;
    this.assignmentHandler = assignmentHandler;
    this.conditionalExpressionHandler = conditionalExpressionHandler;
    this.parenthesizedExpressionHandler = parenthesizedExpressionHandler;
    this.nameHandler = nameHandler;
  }

  public CPHandlingResult handle() {
    switch (rightExpressionType) {
      case ASTNode.FIELD_ACCESS: {
        return fieldAccessHandler.handle();
      }
      case ASTNode.METHOD_INVOCATION: {
        return methodInvocationHandler.handle();
      }
      case ASTNode.SUPER_FIELD_ACCESS: {
        return superFieldAccessHandler.handle();
      }
      case ASTNode.SUPER_METHOD_INVOCATION: {
        return superMethodInvocationHandler.handle();
      }
      case ASTNode.ASSIGNMENT: {
        return assignmentHandler.handle();
      }
      case ASTNode.PARENTHESIZED_EXPRESSION: {
        return parenthesizedExpressionHandler.handle();
      }
      case ASTNode.CONDITIONAL_EXPRESSION: {
        return conditionalExpressionHandler.handle();

      }
      case ASTNode.SIMPLE_NAME:
      case ASTNode.QUALIFIED_NAME: {
        return nameHandler.handle();

      }
      default:
        return new CPHandlingResult(List.of(), Optional.empty());
    }
  }

}

package upt.ac.cti.coverage.analysis.iterative.iterators.handlers;

import upt.ac.cti.coverage.analysis.iterative.model.CPHandlingResult;
import upt.ac.cti.coverage.analysis.iterative.model.CorelationPair;

//@formatter:off
abstract class FieldAccessHandler<
H     extends CPFieldHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
FAHT  extends FieldAccessHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
MIHT  extends MethodInvocationHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
SFAHT extends SuperFieldAccessHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
SMIHT extends SuperMethodInvocationHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
AHT   extends AssignmentHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
PEHT  extends ParenthesizedExpressionHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
CEHT  extends ConditionalExpressionHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
NHT   extends NameHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>
> extends RightSideHandler {
//@formatter:on

  public FieldAccessHandler(CorelationPair cp) {
    super(cp);
  }
}


//@formatter:off
class FieldAccess1Handler extends FieldAccessHandler<
CPField1Handler,
FieldAccess1Handler,
MethodInvocation1Handler,
SuperFieldAccess1Handler,
SuperMethodInvocation1Handler,
Assignment1Handler,
ParenthesizedExpression1Handler,
ConditionalExpression1Handler,
Name1Handler
> {
//@formatter:on

  public FieldAccess1Handler(CorelationPair cp) {
    super(cp);
  }

  @Override
  public CPHandlingResult handle() {
    return voidResult();
  }
}


//@formatter:off
class FieldAccess2Handler extends FieldAccessHandler<
CPField2Handler,
FieldAccess2Handler,
MethodInvocation2Handler,
SuperFieldAccess2Handler,
SuperMethodInvocation2Handler,
Assignment2Handler,
ParenthesizedExpression2Handler,
ConditionalExpression2Handler,
Name2Handler
>{
//@formatter:on

  public FieldAccess2Handler(CorelationPair cp) {
    super(cp);
  }

  @Override
  public CPHandlingResult handle() {
    return voidResult();
  }
}

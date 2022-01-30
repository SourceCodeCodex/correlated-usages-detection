package upt.ac.cti.coverage.analysis.iterative.iterators.handlers;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import upt.ac.cti.coverage.analysis.iterative.model.CPHandlingResult;
import upt.ac.cti.coverage.analysis.iterative.model.CorelationPair;

//@formatter:off
abstract class ConditionalExpressionHandler<
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

  public ConditionalExpressionHandler(CorelationPair cp) {
    super(cp);
  }
}


//@formatter:off
class ConditionalExpression1Handler extends ConditionalExpressionHandler<
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

  public ConditionalExpression1Handler(CorelationPair cp) {
    super(cp);
  }

  @Override
  public CPHandlingResult handle() {
    var cond = (ConditionalExpression) cp.field1Asgmt().rightSide();
    var newPair1 = cp.withField1Data(cp.field1Asgmt().withRightSide(cond.getThenExpression()));
    var newPair2 = cp.withField1Data(cp.field1Asgmt().withRightSide(cond.getElseExpression()));
    return new CPHandlingResult(List.of(newPair1, newPair2), Optional.empty());
  }
}


//@formatter:off
class ConditionalExpression2Handler extends ConditionalExpressionHandler<
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

  public ConditionalExpression2Handler(CorelationPair cp) {
    super(cp);
  }

  @Override
  public CPHandlingResult handle() {
    var cond = (ConditionalExpression) cp.field2Asgmt().rightSide();
    var newPair1 = cp.withField1Data(cp.field2Asgmt().withRightSide(cond.getThenExpression()));
    var newPair2 = cp.withField1Data(cp.field2Asgmt().withRightSide(cond.getElseExpression()));
    return new CPHandlingResult(List.of(newPair1, newPair2), Optional.empty());
  }
}

package upt.ac.cti.coverage.analysis.iterative.iterators.handlers;

import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.Assignment;
import upt.ac.cti.coverage.analysis.iterative.model.CPHandlingResult;
import upt.ac.cti.coverage.analysis.iterative.model.CorelationPair;

//@formatter:off
abstract class AssignmentHandler<
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
  public AssignmentHandler(CorelationPair cp) {
    super(cp);
  }
}


//@formatter:off
class Assignment1Handler extends AssignmentHandler<
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

  public Assignment1Handler(CorelationPair cp) {
    super(cp);
  }

  @Override
  public CPHandlingResult handle() {
    var asgmt = (Assignment) cp.field1Asgmt().rightSide();
    var newPair = cp.withField1Data(cp.field1Asgmt().withRightSide(asgmt.getRightHandSide()));
    return new CPHandlingResult(List.of(newPair), Optional.empty());
  }
}


//@formatter:off
class Assignment2Handler extends AssignmentHandler<
CPField2Handler,
FieldAccess2Handler,
MethodInvocation2Handler,
SuperFieldAccess2Handler,
SuperMethodInvocation2Handler,
Assignment2Handler,
ParenthesizedExpression2Handler,
ConditionalExpression2Handler,
Name2Handler
> {
//@formatter:on

  public Assignment2Handler(CorelationPair cp) {
    super(cp);
  }

  @Override
  public CPHandlingResult handle() {
    var asgmt = (Assignment) cp.field2Asgmt().rightSide();
    var newPair = cp.withField2Data(cp.field2Asgmt().withRightSide(asgmt.getRightHandSide()));
    return new CPHandlingResult(List.of(newPair), Optional.empty());
  }
}

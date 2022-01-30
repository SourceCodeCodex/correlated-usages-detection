package upt.ac.cti.coverage.analysis.iterative.iterators.handlers;

import upt.ac.cti.coverage.analysis.iterative.model.CorelationPair;

//@formatter:off
public class CPField1Handler extends CPFieldHandler<
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

  public CPField1Handler(CorelationPair cp) {
    super(
        cp.field1Asgmt().rightSide().getNodeType(),
        new FieldAccess1Handler(cp),
        new MethodInvocation1Handler(cp),
        new SuperFieldAccess1Handler(cp),
        new SuperMethodInvocation1Handler(cp),
        new Assignment1Handler(cp),
        new ParenthesizedExpression1Handler(cp),
        new ConditionalExpression1Handler(cp),
        new Name1Handler(cp));
  }

}

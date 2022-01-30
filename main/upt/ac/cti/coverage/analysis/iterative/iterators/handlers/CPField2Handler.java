package upt.ac.cti.coverage.analysis.iterative.iterators.handlers;

import upt.ac.cti.coverage.analysis.iterative.model.CorelationPair;

//@formatter:off
public class CPField2Handler extends CPFieldHandler<
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
//@formatter:off

  public CPField2Handler(CorelationPair cp) {
    super(
        cp.field2Asgmt().rightSide().getNodeType(),
        new FieldAccess2Handler(cp),
        new MethodInvocation2Handler(cp),
        new SuperFieldAccess2Handler(cp),
        new SuperMethodInvocation2Handler(cp),
        new Assignment2Handler(cp),
        new ParenthesizedExpression2Handler(cp),
        new ConditionalExpression2Handler(cp),
        new Name2Handler(cp));
  }

}

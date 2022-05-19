package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.simple;

import java.util.logging.Logger;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.ISimpleWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.FieldAccessDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.MethodInvocationDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.SuperFieldAccessDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.SuperMethodInvocationDerivator;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;
import upt.ac.cti.util.logging.RLogger;

final public class SimpleWritingsDerivator<J extends IJavaElement>
    implements ISimpleWritingsDerivator<J> {

  private final FieldAccessDerivator<J> fa;
  private final MethodInvocationDerivator<J> mi;
  private final SuperFieldAccessDerivator<J> sfa;
  private final SuperMethodInvocationDerivator<J> smi;
  private final AssignmentDerivator<J> a;
  private final ParenthesizedExpressionDerivator<J> pe;
  private final ConditionalExpressionDerivator<J> ce;
  private final NameDerivator<J> n;
  private final CastDerivator<J> c;

  private static final Logger logger = RLogger.get();

  public SimpleWritingsDerivator() {
    this.fa = new FieldAccessDerivator<>();
    this.mi = new MethodInvocationDerivator<>();
    this.sfa = new SuperFieldAccessDerivator<>();
    this.smi = new SuperMethodInvocationDerivator<>();
    this.a = new AssignmentDerivator<>();
    this.pe = new ParenthesizedExpressionDerivator<>();
    this.ce = new ConditionalExpressionDerivator<>();
    this.n = new NameDerivator<>();
    this.c = new CastDerivator<>();
  }



  @Override
  public NewWritingPairs<J> derive(DerivableWriting<J> deriver, Writing<J> constant) {
    var expressionType = deriver.writingExpression().getNodeType();
    switch (expressionType) {
      case ASTNode.FIELD_ACCESS: {
        return fa.derive(deriver, constant);
      }
      case ASTNode.METHOD_INVOCATION: {
        return mi.derive(deriver, constant);
      }
      case ASTNode.SUPER_FIELD_ACCESS: {
        return sfa.derive(deriver, constant);
      }
      case ASTNode.SUPER_METHOD_INVOCATION: {
        return smi.derive(deriver, constant);
      }
      case ASTNode.ASSIGNMENT: {
        return a.derive(deriver, constant);
      }
      case ASTNode.PARENTHESIZED_EXPRESSION: {
        return pe.derive(deriver, constant);
      }
      case ASTNode.CONDITIONAL_EXPRESSION: {
        return ce.derive(deriver, constant);
      }
      case ASTNode.CAST_EXPRESSION: {
        return c.derive(deriver, constant);
      }
      case ASTNode.SIMPLE_NAME:
      case ASTNode.QUALIFIED_NAME: {
        return n.derive(deriver, constant);
      }
      case ASTNode.LAMBDA_EXPRESSION: {
        return NewWritingPairs.NULL();
      }
      default: {
        logger.warning("No derivation is possible for " + deriver + ". Writing expression type is "
            + expressionType);
        return NewWritingPairs.NULL();
      }
    }
  }
}

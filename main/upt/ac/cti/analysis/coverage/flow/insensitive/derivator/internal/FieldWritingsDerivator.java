package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal;

import java.util.logging.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.NewWritingPairs;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.JavaEntitySearcher;

final public class FieldWritingsDerivator implements IFieldWritingsDerivator {

  private final FieldAccessDerivator fa;
  private final MethodInvocationDerivator mi;
  private final SuperFieldAccessDerivator sfa;
  private final SuperMethodInvocationDerivator smi;
  private final AssignmentDerivator a;
  private final ParenthesizedExpressionDerivator pe;
  private final ConditionalExpressionDerivator ce;
  private final NameDerivator n;

  private static final Logger logger =
      Logger.getLogger(FieldWritingsDerivator.class.getSimpleName());;

  public FieldWritingsDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.fa = new FieldAccessDerivator(javaEntitySearcher, codeParser);;
    this.mi = new MethodInvocationDerivator(codeParser);
    this.sfa = new SuperFieldAccessDerivator(javaEntitySearcher, codeParser);
    this.smi = new SuperMethodInvocationDerivator(codeParser);
    this.a = new AssignmentDerivator();
    this.pe = new ParenthesizedExpressionDerivator();
    this.ce = new ConditionalExpressionDerivator();
    this.n = new NameDerivator(javaEntitySearcher, codeParser);
  }



  @Override
  public DerivationResult derive(FieldWriting deriver, FieldWriting constant) {
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
      case ASTNode.SIMPLE_NAME:
      case ASTNode.QUALIFIED_NAME: {
        return n.derive(deriver, constant);

      }
      default: {
        logger.warning("No derivation is possible for " + deriver + ". Writing expression type is "
            + expressionType);
        return NewWritingPairs.NULL;
      }
    }
  }
}

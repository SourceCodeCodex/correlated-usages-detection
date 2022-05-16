package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import java.util.logging.Logger;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import org.javatuples.Pair;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.IWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;
import upt.ac.cti.util.logging.RLogger;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

public final class ComplexWritingsDerivator<J extends IJavaElement>
    implements IWritingsDerivator<J> {

  private final MMDerivator<J> mm;
  private final FFDerivator<J> ff;
  private final FMDerivator<J> fm;
  private final FPDerivator<J> fp;
  private final MPDerivator<J> mp;
  private final PPDerivator<J> pp;

  private static final Logger logger = RLogger.get();

  public ComplexWritingsDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.mm = new MMDerivator<>(codeParser);
    this.ff = new FFDerivator<>(javaEntitySearcher, codeParser);
    this.fm = new FMDerivator<>(javaEntitySearcher, codeParser);
    this.fp = new FPDerivator<>(javaEntitySearcher, codeParser);
    this.mp = new MPDerivator<>(javaEntitySearcher, codeParser);
    this.pp = new PPDerivator<>(javaEntitySearcher, codeParser);
  }

  @Override
  public NewWritingPairs<J> derive(Writing<J> w1, Writing<J> w2) {
    if (isMethodInvocation(w1) && isMethodInvocation(w2)) {
      return mm.derive(w1, w2);
    }

    if (isField(w1) && isField(w2)) {
      return ff.derive(w1, w2);
    }

    if (isField(w1) && isMethodInvocation(w2)) {
      return fm.derive(w1, w2);
    }

    if (isMethodInvocation(w1) && isField(w2)) {
      return swap(fm.derive(w2, w1));
    }

    if (isField(w1) && isParameter(w2)) {
      return fp.derive(w1, w2);
    }

    if (isParameter(w1) && isField(w2)) {
      return swap(fp.derive(w2, w1));
    }

    if (isMethodInvocation(w1) && isParameter(w2)) {
      return mp.derive(w1, w2);
    }

    if (isParameter(w1) && isMethodInvocation(w2)) {
      return swap(mp.derive(w2, w1));
    }

    if (isParameter(w1) && isParameter(w2)) {
      return pp.derive(w1, w2);
    }

    logger.warning("No complex derivation is possible for pair: (" + w1 + ",  " + w2 + ")");
    return NewWritingPairs.NULL();

  }

  private NewWritingPairs<J> swap(NewWritingPairs<J> pairings) {
    return new NewWritingPairs<>(pairings.writingPairs().stream()
        .map(p -> Pair.with(p.getValue1(), p.getValue0()))
        .toList());
  }

  private boolean isMethodInvocation(Writing<J> w) {
    var nodeType = w.writingExpression().getNodeType();
    return nodeType == ASTNode.METHOD_INVOCATION || nodeType == ASTNode.SUPER_METHOD_INVOCATION;
  }

  private boolean isField(Writing<J> w) {
    var nodeType = w.writingExpression().getNodeType();
    if (nodeType == ASTNode.FIELD_ACCESS || nodeType == ASTNode.SUPER_FIELD_ACCESS) {
      return true;
    }

    if (nodeType == ASTNode.SIMPLE_NAME || nodeType == ASTNode.QUALIFIED_NAME) {
      var name = (Name) w.writingExpression();
      var binding = name.resolveBinding();

      return ((IVariableBinding) binding).isField();
    }

    return false;

  }

  private boolean isParameter(Writing<J> w) {
    var nodeType = w.writingExpression().getNodeType();
    if (nodeType == ASTNode.SIMPLE_NAME || nodeType == ASTNode.QUALIFIED_NAME) {
      var name = (Name) w.writingExpression();
      var binding = name.resolveBinding();

      return ((IVariableBinding) binding).isParameter();
    }

    return false;

  }
}

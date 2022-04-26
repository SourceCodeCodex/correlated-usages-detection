package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.MethodInvocation;
import upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.algorithm.InvocationDerivatorAlgorithm;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;

final class MethodInvocationDerivator implements IFieldWritingsDerivator {

  private final InvocationDerivatorAlgorithm algorithm;

  public MethodInvocationDerivator(CodeParser codeParser) {
    this.algorithm = new InvocationDerivatorAlgorithm(codeParser);
  }

  @Override
  public DerivationResult derive(FieldWriting deriver, FieldWriting constant) {
    var methodInvocation = (MethodInvocation) deriver.writingExpression();
    var method = (IMethod) methodInvocation.resolveMethodBinding().getJavaElement();

    return algorithm.derive(deriver, constant, method);
  }
}



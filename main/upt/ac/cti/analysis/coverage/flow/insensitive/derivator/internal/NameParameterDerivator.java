package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal;

import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.Name;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.visitors.ArgumentMethodInvocationVisitor;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.NewWritingPairs;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.JavaEntitySearcher;

final public class NameParameterDerivator implements IFieldWritingsDerivator {

  private final JavaEntitySearcher javaEntitySearcher;
  private final CodeParser codeParser;

  public NameParameterDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.javaEntitySearcher = javaEntitySearcher;
    this.codeParser = codeParser;
  }

  @Override
  public DerivationResult derive(FieldWriting deriver, FieldWriting constant) {
    var name = (Name) deriver.writingExpression();
    var binding = name.resolveBinding();

    var parameter =
        (ILocalVariable) binding.getJavaElement();

    var invocations =
        javaEntitySearcher.searchMethodInvocations((IMethod) parameter.getDeclaringMember());

    var derivations = invocations.stream().flatMap(it -> {
      var visitor = new ArgumentMethodInvocationVisitor(deriver, parameter);
      var node = codeParser.parse(it.getCompilationUnit());

      node.accept(visitor);
      return visitor.derivations().stream();
    });

    var newWritingPairs = derivations
        .map(derivation -> Pair.with(derivation, constant))
        .toList();

    return new NewWritingPairs(newWritingPairs);
  }

}

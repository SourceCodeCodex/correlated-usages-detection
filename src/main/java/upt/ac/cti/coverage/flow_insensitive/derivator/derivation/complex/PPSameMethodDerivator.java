package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import java.util.stream.Stream;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.Name;
import org.javatuples.Pair;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.IComplexWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex.visitor.BothArgumentsInvocationVisitor;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

class PPSameMethodDerivator<J extends IJavaElement> implements IComplexWritingsDerivator<J> {

  private final JavaEntitySearcher javaEntitySearcher = Dependencies.javaEntitySearcher;
  private final CodeParser codeParser = Dependencies.codeParser;


  @Override
  public NewWritingPairs<J> derive(DerivableWriting<J> w1, DerivableWriting<J> w2) {
    var p1 =
        (ILocalVariable) ((Name) w1.writingExpression()).resolveBinding().getJavaElement();
    var p2 =
        (ILocalVariable) ((Name) w2.writingExpression()).resolveBinding().getJavaElement();

    var invocations =
        javaEntitySearcher.searchMethodInvocations((IMethod) p1.getDeclaringMember());

    var newWritingPairs = invocations.stream().flatMap(it -> {
      var visitor = new BothArgumentsInvocationVisitor<>(w1, w2, p1, p2);
      if (it.getCompilationUnit() == null) {
        return Stream.empty();
      }
      var nodeOpt = codeParser.parse(it.getCompilationUnit());

      return nodeOpt.map(node -> {
        node.accept(visitor);
        return visitor.newPairings().stream();
      }).orElse(Stream.empty());
    }).toList();

    if (newWritingPairs.isEmpty()) {
      return NewWritingPairs
          .of(Pair.with(w1.toUnderivableWriting(), w2.toUnderivableWriting()));
    }

    return new NewWritingPairs<>(newWritingPairs);
  }


}

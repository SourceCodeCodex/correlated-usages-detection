package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import java.util.ArrayList;
import java.util.stream.Stream;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.Name;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.IWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.visitor.ArgumentMethodInvocationVisitor;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.computation.CartesianProduct;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

class PPDifferentMethodDerivator<J extends IJavaElement> implements IWritingsDerivator<J> {
  private final JavaEntitySearcher javaEntitySearcher = Dependencies.javaEntitySearcher;
  private final CodeParser codeParser = Dependencies.codeParser;

  @Override
  public NewWritingPairs<J> derive(Writing<J> w1, Writing<J> w2) {
    var p1 = (ILocalVariable) ((Name) w1.writingExpression()).resolveBinding().getJavaElement();
    var p2 = (ILocalVariable) ((Name) w2.writingExpression()).resolveBinding().getJavaElement();

    var invocations1 =
        javaEntitySearcher.searchMethodInvocations((IMethod) p1.getDeclaringMember());

    var invocations2 =
        javaEntitySearcher.searchMethodInvocations((IMethod) p2.getDeclaringMember());

    var commonScope = new ArrayList<>(invocations1);
    commonScope.retainAll(invocations2);

    var newWritingPairs = commonScope.stream().flatMap(it -> {
      var visitor1 = new ArgumentMethodInvocationVisitor<>(w1, p1);
      var visitor2 = new ArgumentMethodInvocationVisitor<>(w2, p2);

      if (it.getCompilationUnit() == null) {
        return Stream.empty();
      }

      var nodeOpt = codeParser.parse(it.getCompilationUnit());

      return nodeOpt.map(node -> {
        node.accept(visitor1);
        node.accept(visitor2);

        var derivation1 = visitor1.derivations().stream().map(Writing::increaseDepth).toList();
        var derivation2 = visitor2.derivations().stream().map(Writing::increaseDepth).toList();

        return CartesianProduct.product(derivation1, derivation2).stream();
      }).orElse(Stream.empty());
    }).toList();


    return new NewWritingPairs<>(newWritingPairs);
  }
}

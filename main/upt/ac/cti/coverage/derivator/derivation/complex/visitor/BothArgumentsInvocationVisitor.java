package upt.ac.cti.coverage.derivator.derivation.complex.visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.javatuples.Pair;
import upt.ac.cti.coverage.model.Writing;

public class BothArgumentsInvocationVisitor<J extends IJavaElement> extends ASTVisitor {
  private final Writing<J> w1, w2;
  private final ILocalVariable p1, p2;

  private final List<Pair<Writing<J>, Writing<J>>> newPairings = new ArrayList<>();

  public List<Pair<Writing<J>, Writing<J>>> newPairings() {
    return newPairings;
  }

  public BothArgumentsInvocationVisitor(Writing<J> w1, Writing<J> w2, ILocalVariable p1,
      ILocalVariable p2) {
    this.w1 = w1;
    this.w2 = w2;
    this.p1 = p1;
    this.p2 = p2;
  }

  @Override
  public boolean visit(ClassInstanceCreation node) {
    var binding = node.resolveConstructorBinding();
    var je = binding.getJavaElement();

    if (binding != null
        && je != null
        && je.equals(p1.getDeclaringMember())
        && je.equals(p2.getDeclaringMember())) {
      try {
        var method = (IMethod) p1.getDeclaringMember();
        var index1 = Arrays.asList(method.getParameters()).indexOf(p1);
        var index2 = Arrays.asList(method.getParameters()).indexOf(p2);

        var arg1 = (Expression) node.arguments().get(index1);
        var arg2 = (Expression) node.arguments().get(index2);

        var derivation1 = w1.withWritingExpression(arg1).withAccessExpression(node).increaseDepth();
        var derivation2 = w2.withWritingExpression(arg2).withAccessExpression(node).increaseDepth();

        newPairings.add(
            Pair.with(derivation1, derivation2));
      } catch (JavaModelException e) {
        e.printStackTrace();
      }

    }
    return true;
  }

  @Override
  public boolean visit(MethodInvocation node) {
    var binding = node.resolveMethodBinding();
    var je = binding.getJavaElement();

    if (binding != null
        && je != null
        && je.equals(p1.getDeclaringMember())
        && je.equals(p2.getDeclaringMember())) {
      try {
        var method = (IMethod) p1.getDeclaringMember();
        var index1 = Arrays.asList(method.getParameters()).indexOf(p1);
        var index2 = Arrays.asList(method.getParameters()).indexOf(p2);

        var arg1 = (Expression) node.arguments().get(index1);
        var arg2 = (Expression) node.arguments().get(index2);

        var derivation1 = w1.withWritingExpression(arg1).withAccessExpression(node.getExpression())
            .increaseDepth();
        var derivation2 = w2.withWritingExpression(arg2).withAccessExpression(node.getExpression())
            .increaseDepth();

        newPairings.add(
            Pair.with(derivation1, derivation2));

      } catch (JavaModelException e) {
        e.printStackTrace();
      }

    }
    return true;
  }

  @Override
  public boolean visit(SuperMethodInvocation node) {
    var binding = node.resolveMethodBinding();
    var je = binding.getJavaElement();

    if (binding != null
        && je != null
        && je.equals(p1.getDeclaringMember())
        && je.equals(p2.getDeclaringMember())) {
      try {
        var method = (IMethod) p1.getDeclaringMember();
        var index1 = Arrays.asList(method.getParameters()).indexOf(p1);
        var index2 = Arrays.asList(method.getParameters()).indexOf(p2);

        var arg1 = (Expression) node.arguments().get(index1);
        var arg2 = (Expression) node.arguments().get(index2);

        var derivation1 = w1.withWritingExpression(arg1)
            .increaseDepth();
        var derivation2 = w2.withWritingExpression(arg2)
            .increaseDepth();

        newPairings.add(
            Pair.with(derivation1, derivation2));

      } catch (JavaModelException e) {
        e.printStackTrace();
      }
    }
    return true;
  }



}

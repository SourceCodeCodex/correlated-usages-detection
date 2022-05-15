package upt.ac.cti.coverage.derivator.derivation.shared.visitor;

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
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.util.computation.Either;

public class ArgumentMethodInvocationVisitor<J extends IJavaElement> extends ASTVisitor {

  private final Writing<J> deriver;
  private final ILocalVariable localVar;

  private final List<Writing<J>> derivations = new ArrayList<>();

  public ArgumentMethodInvocationVisitor(Writing<J> deriver, ILocalVariable localVar) {
    this.deriver = deriver;
    this.localVar = localVar;
  }

  public List<Writing<J>> derivations() {
    return derivations;
  }

  @Override
  public boolean visit(ClassInstanceCreation node) {
    var binding = node.resolveConstructorBinding();
    var je = binding.getJavaElement();
    if (binding != null && je != null
        && je.equals(localVar.getDeclaringMember())) {
      try {
        var method = (IMethod) localVar.getDeclaringMember();
        var index = Arrays.asList(method.getParameters()).indexOf(localVar);
        if (index >= 0) {
          var arg = (Expression) node.arguments().get(index);
          var derivation =
              deriver.withWritingExpression(arg).withAccessExpression(Either.right(node));
          derivations.add(derivation);
        }
      } catch (JavaModelException e) {
        e.printStackTrace();
      }

    }
    return false;
  }

  @Override
  public boolean visit(MethodInvocation node) {
    var binding = node.resolveMethodBinding();
    var je = binding.getJavaElement();
    if (binding != null && je != null
        && je.equals(localVar.getDeclaringMember())) {
      try {
        var method = (IMethod) localVar.getDeclaringMember();
        var index = Arrays.asList(method.getParameters()).indexOf(localVar);
        if (index >= 0) {
          var arg = (Expression) node.arguments().get(index);
          var derivation =
              deriver.withWritingExpression(arg);
          if (node.getExpression() != null) {
            derivation = derivation.withAccessExpression(Either.right(node.getExpression()));
          }
          derivations.add(derivation);
        }
      } catch (JavaModelException e) {
        e.printStackTrace();
      }

    }
    return false;
  }

  @Override
  public boolean visit(SuperMethodInvocation node) {
    var binding = node.resolveMethodBinding();
    var je = binding.getJavaElement();
    if (binding != null && je != null
        && je.equals(localVar.getDeclaringMember())) {
      try {
        var method = (IMethod) localVar.getDeclaringMember();
        var index = Arrays.asList(method.getParameters()).indexOf(localVar);
        if (index >= 0) {
          var arg = (Expression) node.arguments().get(index);
          var derivation = deriver.withWritingExpression(arg);
          derivations.add(derivation);
        }
      } catch (JavaModelException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

}

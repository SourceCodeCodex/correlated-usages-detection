package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.visitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

public class MethodInvocationVisitor extends ASTVisitor {

  private final ILocalVariable localVar;

  private final List<Expression> result = new ArrayList<>();

  public MethodInvocationVisitor(ILocalVariable localVar) {
    this.localVar = localVar;
  }

  public List<Expression> result() {
    return result;
  }

  @Override
  public boolean visit(ClassInstanceCreation node) {
    if (localVar.getDeclaringMember().equals(node.resolveConstructorBinding().getJavaElement())) {
      try {
        var iMethod = (IMethod) localVar.getDeclaringMember();
        var index = Arrays.asList(iMethod.getParameters()).indexOf(localVar);
        result.add((Expression) node.arguments().get(index));
      } catch (JavaModelException e) {
        e.printStackTrace();
      }

    }
    return true;
  }

  @Override
  public boolean visit(MethodInvocation node) {
    if (localVar.getDeclaringMember().equals(node.resolveMethodBinding().getJavaElement())) {
      try {
        var iMethod = (IMethod) localVar.getDeclaringMember();
        var index = Arrays.asList(iMethod.getParameters()).indexOf(localVar);
        result.add((Expression) node.arguments().get(index));
      } catch (JavaModelException e) {
        e.printStackTrace();
      }

    }
    return true;
  }

  @Override
  public boolean visit(SuperMethodInvocation node) {
    if (localVar.getDeclaringMember().equals(node.resolveMethodBinding().getJavaElement())) {
      try {
        var iMethod = (IMethod) localVar.getDeclaringMember();
        var index = Arrays.asList(iMethod.getParameters()).indexOf(localVar);
        result.add((Expression) node.arguments().get(index));
      } catch (JavaModelException e) {
        e.printStackTrace();
      }
    }
    return true;
  }

}

package upt.ac.cti.coverage.combiner.parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.javatuples.Pair;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.util.Either;

public class BothArgumentsInvocationVisitor extends ASTVisitor {
  private final ILocalVariable p1, p2;

  private final List<Pair<Writing<ILocalVariable>, Writing<ILocalVariable>>> newPairings =
      new ArrayList<>();

  public List<Pair<Writing<ILocalVariable>, Writing<ILocalVariable>>> newPairings() {
    return newPairings;
  }

  public BothArgumentsInvocationVisitor(ILocalVariable p1, ILocalVariable p2) {
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

        var derivation1 = new Writing<>(p1, arg1, Either.right(node));
        var derivation2 = new Writing<>(p2, arg2, Either.right(node));

        newPairings.add(Pair.with(derivation1, derivation2));
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

        Either<IMember, Expression> accesExpression =
            node.getExpression() != null ? Either.right(node.getExpression())
                : Either.left(method.getDeclaringType());

        var derivation1 = new Writing<>(p1, arg1, accesExpression);
        var derivation2 = new Writing<>(p2, arg2, accesExpression);

        newPairings.add(Pair.with(derivation1, derivation2));

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

        var derivation1 = new Writing<>(p1, arg1, Either.left(method.getDeclaringType()));
        var derivation2 = new Writing<>(p2, arg2, Either.left(method.getDeclaringType()));

        newPairings.add(Pair.with(derivation1, derivation2));

      } catch (JavaModelException e) {
        e.printStackTrace();
      }
    }
    return false;
  }



}

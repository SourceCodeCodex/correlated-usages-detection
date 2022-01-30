package upt.ac.cti.coverage.analysis.iterative.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import upt.ac.cti.coverage.analysis.iterative.model.ConcreteObject;
import upt.ac.cti.coverage.analysis.iterative.model.FieldAsgmt;
import upt.ac.cti.coverage.analysis.iterative.model.This;

public class AsgmtVisitor extends ASTVisitor {
  // Either IField or ILocalVariable
  private final IJavaElement writeElement;

  private final IField iField;
  private final IMethod iMethod;

  private final List<FieldAsgmt> result = new ArrayList<>();

  public AsgmtVisitor(IJavaElement writeElement, IField iField, IMethod iMethod) {
    this.writeElement = writeElement;
    this.iField = iField;
    this.iMethod = iMethod;
  }

  @Override
  public boolean visit(Assignment node) {
    return handleLefSide(node, node.getLeftHandSide());
  }

  public List<FieldAsgmt> result() {
    return result;
  }

  // Needed in case of parenthesized epxression - recursive
  private boolean handleLefSide(Assignment node, Expression leftSide) {
    var leftExpressionType = leftSide.getNodeType();
    switch (leftExpressionType) {
      case ASTNode.SIMPLE_NAME: {
        var binding = ((SimpleName) leftSide).resolveBinding();
        if (binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (writeElement.equals(varBinding.getJavaElement())) {
            var state = FieldAsgmt.builder()
                .field(iField)
                .baseObject(This.instance())
                .currentMethod(iMethod)
                .rightSide(node.getRightHandSide())
                .build();
            result.add(state);

          }
        }
        break;
      }
      case ASTNode.QUALIFIED_NAME: {
        var binding = ((QualifiedName) leftSide).resolveBinding();
        if (binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (writeElement.equals(varBinding.getJavaElement())) {
            var state = FieldAsgmt.builder()
                .field(iField)
                .baseObject(new ConcreteObject(((QualifiedName) leftSide).getQualifier()))
                .currentMethod(iMethod)
                .rightSide(node.getRightHandSide())
                .build();
            result.add(state);

          }
        }
        break;
      }
      case ASTNode.FIELD_ACCESS: {
        var varBinding = ((FieldAccess) leftSide).resolveFieldBinding();
        if (writeElement.equals(varBinding.getJavaElement())) {
          var expr = ((FieldAccess) leftSide).getExpression();
          if (expr.getNodeType() == ASTNode.THIS_EXPRESSION) {
            var state = FieldAsgmt.builder()
                .field(iField)
                .baseObject(This.instance())
                .currentMethod(iMethod)
                .rightSide(node.getRightHandSide())
                .build();
            result.add(state);
          } else {
            var state = FieldAsgmt.builder()
                .field(iField)
                .baseObject(new ConcreteObject(expr))
                .currentMethod(iMethod)
                .rightSide(node.getRightHandSide())
                .build();
            result.add(state);
          }
        }
        break;
      }
      case ASTNode.PARENTHESIZED_EXPRESSION: {
        handleLefSide(node, ((ParenthesizedExpression) leftSide).getExpression());
      }
      default:
        throw new IllegalArgumentException(
            "Analysis not implemented for left hand side of type: " + leftExpressionType + " "
                + leftSide);
    }
    return true;
  }

}

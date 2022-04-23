package upt.ac.cti.analysis.coverage.flow.insensitive.combiner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;

class FieldWritingsVisitor extends ASTVisitor {

  private final IField field;

  private final List<FieldWriting> result = new ArrayList<>();

  private static final Logger logger = Logger.getLogger(FieldWritingsVisitor.class.getName());

  public FieldWritingsVisitor(IField field) {
    this.field = field;
  }

  public List<FieldWriting> result() {
    return result;
  }

  @Override
  public boolean visit(Assignment node) {
    var left = node.getLeftHandSide();

    switch (left.getNodeType()) {

      case ASTNode.SIMPLE_NAME: {
        var binding = ((SimpleName) left).resolveBinding();
        if (binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (field.equals(varBinding.getJavaElement())) {
            var fieldWrite = new FieldWriting(field, node.getRightHandSide(), Optional.empty());
            result.add(fieldWrite);
          }
        }
        break;
      }

      case ASTNode.QUALIFIED_NAME: {
        var binding = ((QualifiedName) left).resolveBinding();
        var qualifier = ((QualifiedName) left).getQualifier();
        if (binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (field.equals(varBinding.getJavaElement())) {
            var fieldWrite =
                new FieldWriting(field, node.getRightHandSide(), Optional.of(qualifier));
            result.add(fieldWrite);
          }
        }
        break;
      }

      case ASTNode.FIELD_ACCESS: {
        var varBinding = ((FieldAccess) left).resolveFieldBinding();
        if (field.equals(varBinding.getJavaElement())) {
          var accessExpression = ((FieldAccess) left).getExpression();
          if (accessExpression.getNodeType() == ASTNode.THIS_EXPRESSION) {
            var fieldWrite = new FieldWriting(field, node.getRightHandSide(), Optional.empty());
            result.add(fieldWrite);
          } else {
            var fieldWrite =
                new FieldWriting(field, node.getRightHandSide(), Optional.of(accessExpression));
            result.add(fieldWrite);
          }
        }
        break;
      }

      default: {
        if (field.equals(left.resolveTypeBinding().getJavaElement())) {
          logger.warning(
              "Assignment omitted as there is no handling method for assignments with this left side expression type: "
                  + left.getNodeType());
        }
      }
    }
    return true;
  }

}

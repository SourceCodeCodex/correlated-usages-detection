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

  private static final Logger logger = Logger.getLogger(FieldWritingsVisitor.class.getName());

  private final IField field;

  private final List<FieldWriting> result = new ArrayList<>();

  public FieldWritingsVisitor(IField field) {
    this.field = field;
  }

  public List<FieldWriting> result() {
    return result;
  }

  @Override
  public boolean visit(Assignment node) {
    var leftExpression = node.getLeftHandSide();

    switch (leftExpression.getNodeType()) {

      case ASTNode.SIMPLE_NAME: {
        var binding = ((SimpleName) leftExpression).resolveBinding();
        if (binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (field.equals(varBinding.getJavaElement())) {
            var fieldWrite = new FieldWriting(field, node.getRightHandSide(), Optional.empty());
            result.add(fieldWrite);
            logger.info("Found SIMPLE_NAME field write: " + node.toString());
          }
        }
        break;
      }

      case ASTNode.QUALIFIED_NAME: {
        var binding = ((QualifiedName) leftExpression).resolveBinding();
        var qualifier = ((QualifiedName) leftExpression).getQualifier();
        if (binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (field.equals(varBinding.getJavaElement())) {
            var fieldWrite =
                new FieldWriting(field, node.getRightHandSide(), Optional.of(qualifier));
            result.add(fieldWrite);
            logger.info("Found QUALIFIED_NAME field write: " + node.toString());
          }
        }
        break;
      }

      case ASTNode.FIELD_ACCESS: {
        var varBinding = ((FieldAccess) leftExpression).resolveFieldBinding();
        if (field.equals(varBinding.getJavaElement())) {
          var accessExpression = ((FieldAccess) leftExpression).getExpression();
          if (accessExpression.getNodeType() == ASTNode.THIS_EXPRESSION) {
            var fieldWrite = new FieldWriting(field, node.getRightHandSide(), Optional.empty());
            result.add(fieldWrite);
          } else {
            var fieldWrite =
                new FieldWriting(field, node.getRightHandSide(), Optional.of(accessExpression));
            result.add(fieldWrite);
          }
          logger.info("Found FIELD_ACCESS field write: " + node.toString());
        }
        break;
      }

      default: {
        logger.warning(
            "Assignment omitted as there is no handling method for assignments with this left side expression type! Expression type : "
                + leftExpression.getNodeType());
      }
    }
    return true;
  }

}

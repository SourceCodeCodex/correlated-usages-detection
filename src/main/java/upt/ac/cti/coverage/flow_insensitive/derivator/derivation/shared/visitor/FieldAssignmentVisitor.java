package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.shared.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.util.logging.RLogger;

public class FieldAssignmentVisitor<J extends IJavaElement> extends ASTVisitor {

  private final DerivableWriting<J> deriver;
  private final IField field;

  private final List<DerivableWriting<J>> derivations = new ArrayList<>();

  private static final Logger logger = RLogger.get();

  public FieldAssignmentVisitor(DerivableWriting<J> deriver, IField field) {
    this.deriver = deriver;
    this.field = field;
  }

  public List<DerivableWriting<J>> derivations() {
    return derivations;
  }

  @Override
  public boolean visit(Assignment node) {
    var left = node.getLeftHandSide();
    switch (left.getNodeType()) {
      case ASTNode.SIMPLE_NAME: {
        var binding = ((SimpleName) left).resolveBinding();
        if (binding != null && binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (field.equals(varBinding.getJavaElement())) {
            derivations.add(deriver.withWritingExpression(node.getRightHandSide()));

          }
        }
        break;
      }

      case ASTNode.QUALIFIED_NAME: {
        var binding = ((QualifiedName) left).resolveBinding();
        if (binding != null && binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (field.equals(varBinding.getJavaElement())) {
            derivations.add(deriver.withWritingExpression(node.getRightHandSide()));
          }
        }
        break;
      }

      case ASTNode.SUPER_FIELD_ACCESS: {
        var binding = ((SuperFieldAccess) left).resolveFieldBinding();

        if (binding != null && field.equals(binding.getJavaElement())) {
          derivations.add(deriver.withWritingExpression(node.getRightHandSide()));
        }
        break;
      }

      case ASTNode.FIELD_ACCESS: {
        var binding = ((FieldAccess) left).resolveFieldBinding();

        if (binding != null && field.equals(binding.getJavaElement())) {
          derivations.add(deriver.withWritingExpression(node.getRightHandSide()));
        }
        break;
      }

      case ASTNode.ARRAY_ACCESS: {
        break;
      }

      default: {
        logger.warning(
            "Assignment discarded as left hand side type is not resolved yet: "
                + left.getNodeType());
      }
    }
    return true;
  }
}

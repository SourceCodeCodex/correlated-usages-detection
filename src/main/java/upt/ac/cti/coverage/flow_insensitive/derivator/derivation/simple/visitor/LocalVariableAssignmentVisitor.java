package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.simple.visitor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;

public class LocalVariableAssignmentVisitor<J extends IJavaElement> extends ASTVisitor {

  private final DerivableWriting<J> deriver;
  private final ILocalVariable localVar;

  private final List<DerivableWriting<J>> derivations = new ArrayList<>();

  public LocalVariableAssignmentVisitor(DerivableWriting<J> deriver, ILocalVariable localVar) {
    this.deriver = deriver;
    this.localVar = localVar;
  }

  public List<DerivableWriting<J>> derivations() {
    return derivations;
  }

  @Override
  public boolean visit(VariableDeclarationFragment node) {
    var binding = node.resolveBinding();
    if (binding != null && localVar.equals(binding.getJavaElement())) {
      if (node.getInitializer() != null) {
        derivations.add(deriver.withWritingExpression(node.getInitializer()));
      }
    }
    return false;
  }

  @Override
  public boolean visit(Assignment node) {
    var left = node.getLeftHandSide();
    switch (left.getNodeType()) {
      case ASTNode.SIMPLE_NAME: {
        var binding = ((SimpleName) left).resolveBinding();
        if (binding != null && binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (varBinding != null && localVar.equals(varBinding.getJavaElement())) {
            derivations.add(deriver.withWritingExpression(node.getRightHandSide()));
          }
        }
      }
    }
    return true;
  }


}

package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;

public class LocalVariableAssignmentVisitor extends ASTVisitor {

  private final FieldWriting deriver;
  private final ILocalVariable localVar;

  private final List<FieldWriting> derivations = new ArrayList<>();

  public LocalVariableAssignmentVisitor(FieldWriting deriver, ILocalVariable localVar) {
    this.deriver = deriver;
    this.localVar = localVar;
  }

  public List<FieldWriting> derivations() {
    return derivations;
  }

  @Override
  public boolean visit(VariableDeclarationFragment node) {
    if (localVar.equals(node.resolveBinding().getJavaElement())) {
      if (node.getInitializer() != null) {
        derivations.add(deriver.withWritingExpression(node.getInitializer()));
      }
    }
    return true;
  }

  @Override
  public boolean visit(Assignment node) {
    var left = node.getLeftHandSide();
    switch (left.getNodeType()) {
      case ASTNode.SIMPLE_NAME: {
        var binding = ((SimpleName) left).resolveBinding();
        if (binding.getKind() == IBinding.VARIABLE) {
          var varBinding = (IVariableBinding) binding;
          if (localVar.equals(varBinding.getJavaElement())) {
            derivations.add(deriver.withWritingExpression(node.getRightHandSide()));
          }
        }
      }
    }
    return true;
  }


}

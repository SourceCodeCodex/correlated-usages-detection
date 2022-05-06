package upt.ac.cti.util.parsing.visitor;

import java.util.Optional;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class VariableDeclarationFragmentResolverVisitor
    extends AASTNodeResolverVisitor<IField, VariableDeclarationFragment> {

  public VariableDeclarationFragmentResolverVisitor(IField field) {
    super(field);
  }

  @Override
  public boolean visit(VariableDeclarationFragment node) {
    if (member.equals(node.resolveBinding().getJavaElement())) {
      result = Optional.of(node);
      return false;
    }
    return true;
  }

}

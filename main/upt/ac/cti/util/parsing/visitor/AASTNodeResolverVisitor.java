package upt.ac.cti.util.parsing.visitor;

import java.util.Optional;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

public class AASTNodeResolverVisitor<J extends IMember, B extends ASTNode>
    extends ASTVisitor {

  protected final J member;
  protected Optional<B> result = Optional.empty();

  public AASTNodeResolverVisitor(J javaElement) {
    this.member = javaElement;
  }

  public Optional<B> node() {
    return result;
  }
}

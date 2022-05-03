package upt.ac.cti.util.binding.visitor;

import java.util.Optional;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;

public class ABindingResolverVisitor<J extends IJavaElement, B extends IBinding>
    extends ASTVisitor {

  protected final J member;
  protected Optional<B> binding = Optional.empty();

  public ABindingResolverVisitor(J member) {
    this.member = member;
  }

  public Optional<B> binding() {
    return binding;
  }


}

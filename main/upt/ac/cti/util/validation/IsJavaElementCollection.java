package upt.ac.cti.util.validation;

import java.util.function.Predicate;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import upt.ac.cti.util.binding.ABindingResolver;

public class IsJavaElementCollection<J extends IJavaElement> implements Predicate<J> {
  private final ABindingResolver<J, ITypeBinding> aBindingResolver;

  public IsJavaElementCollection(ABindingResolver<J, ITypeBinding> aBindingResolver) {
    this.aBindingResolver = aBindingResolver;
  }

  @Override
  public boolean test(J t) {
    return aBindingResolver.resolve(t).filter(new IsTypeBindingCollection()).isPresent();
  }



}

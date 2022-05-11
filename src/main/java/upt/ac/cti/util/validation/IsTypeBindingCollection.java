package upt.ac.cti.util.validation;

import java.util.List;
import java.util.function.Predicate;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class IsTypeBindingCollection implements Predicate<ITypeBinding> {

  @Override
  public boolean test(ITypeBinding binding) {
    var interfaces = List.of(binding.getInterfaces());

    var isCollection = interfaces.stream()
        .anyMatch(bind -> bind.getQualifiedName().startsWith("java.util.Collection"));

    return isCollection;

  }

}

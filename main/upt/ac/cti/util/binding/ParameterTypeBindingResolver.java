package upt.ac.cti.util.binding;

import java.util.Optional;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.ITypeBinding;
import upt.ac.cti.util.binding.visitor.ParameterTypeBindingResolverVisitor;

public final class ParameterTypeBindingResolver
    extends ABindingResolver<ILocalVariable, ITypeBinding> {

  @Override
  public Optional<ITypeBinding> resolve(ILocalVariable parameter) {
    var project = parameter.getJavaProject();
    var compilationUnit = parameter.getDeclaringMember().getCompilationUnit();
    var visitor = new ParameterTypeBindingResolverVisitor(parameter);
    return super.resolve(parameter, project, compilationUnit, visitor);
  }

}

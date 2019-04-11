package upt.se.utils.visitors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import upt.se.utils.Parser;

public class GenericParameterBindingVisitor extends ASTVisitor {

  private static Map<ICompilationUnit, HashSet<ITypeBinding>> allTypeBindings = new HashMap<>();
  private HashSet<ITypeBinding> typeBindings = new HashSet<>();

  public boolean visit(SimpleName node) {
    IBinding binding = node.resolveBinding();
    if (binding instanceof ITypeBinding) {
      ITypeBinding type = (ITypeBinding) binding;
      if (!typeBindings.contains(type) && type.isTypeVariable()) {
        typeBindings.add(type);
      }

    }
    return super.visit(node);
  }

  public HashSet<ITypeBinding> getTypeBindings() {
    return typeBindings;
  }

  public static HashSet<ITypeBinding> convert(ICompilationUnit unit) {
    if (allTypeBindings.containsKey(unit)) {
      return allTypeBindings.get(unit);
    }

    GenericParameterBindingVisitor self = new GenericParameterBindingVisitor();

    CompilationUnit cUnit = (CompilationUnit) Parser.parse(unit);
    cUnit.accept(self);

    allTypeBindings.put(unit, self.getTypeBindings());
    return allTypeBindings.get(unit);
  }
}

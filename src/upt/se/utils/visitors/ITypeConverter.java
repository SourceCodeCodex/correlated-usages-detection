package upt.se.utils.visitors;

import static upt.se.utils.helpers.Equals.isEqual;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import io.vavr.control.Option;
import upt.se.utils.Parser;

public class ITypeConverter extends ASTVisitor {
  private IType searchedType;
  private ITypeBinding typeBinding;

  private ITypeConverter(IType searchedType) {
    this.searchedType = searchedType;
  }

  public boolean visit(SimpleName node) {
    IBinding binding = node.resolveBinding();
    if (binding instanceof ITypeBinding) {
      ITypeBinding type = (ITypeBinding) binding;
      if (isEqual(searchedType, type) && !type.isRawType()) {
        typeBinding = type;
      }
    }
    return super.visit(node);
  }

  private Option<ITypeBinding> getTypeBinding() {
    return Option.of(typeBinding);
  }

  public static Option<ITypeBinding> convert(IType type) {
    ITypeConverter self = new ITypeConverter(type);

    CompilationUnit cUnit = (CompilationUnit) Parser.parse(type.getCompilationUnit());
    cUnit.accept(self);

    return self.getTypeBinding();
  }
}

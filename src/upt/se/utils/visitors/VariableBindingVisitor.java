package upt.se.utils.visitors;

import static upt.se.utils.helpers.Equals.isEqual;
import java.util.HashSet;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;

import io.vavr.collection.List;
import upt.se.utils.Parser;

public class VariableBindingVisitor extends ASTVisitor {

  private HashSet<IVariableBinding> attributeBindings = new HashSet<>();
  private HashSet<ITypeBinding> returnTypes = new HashSet<>();
  private ITypeBinding searchedType;

  private VariableBindingVisitor(ITypeBinding searchedType) {
    this.searchedType = searchedType;
  }

  public boolean visit(SimpleName node) {
    IBinding binding = node.resolveBinding();
    if (binding instanceof IVariableBinding) {
      IVariableBinding variable = (IVariableBinding) binding;
      if (!variable.getType().isRawType() && isEqual(variable.getType().getErasure(), searchedType)) {
        attributeBindings.add(variable);
      }
    } else if(binding instanceof IMethodBinding) {
    	IMethodBinding method = (IMethodBinding) binding;
        if (!method.getReturnType().isRawType() && isEqual(method.getReturnType().getErasure(), searchedType)) {
        	returnTypes.add(((IMethodBinding) binding).getReturnType());
        }
    }
    return super.visit(node);
  }

  	public static java.util.List<java.util.List<ITypeBinding>> convert(ICompilationUnit unit, ITypeBinding searchedType) {
	    VariableBindingVisitor self = new VariableBindingVisitor(searchedType);
	    CompilationUnit cUnit = (CompilationUnit) Parser.parse(unit);
	    cUnit.accept(self);
	    List<List<ITypeBinding>> variableUsages = List.ofAll(self.attributeBindings)
				.map(variable -> variable.getType()).map(type -> List.of(type.getTypeArguments()));
		List<List<ITypeBinding>> returnUsages = List.ofAll(self.returnTypes)
				.map(type -> List.of(type.getTypeArguments()));
	    return variableUsages.appendAll(returnUsages).map(List::asJava).asJava();
  	}
}

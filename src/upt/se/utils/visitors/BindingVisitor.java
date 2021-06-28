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

public class BindingVisitor extends ASTVisitor {

	private HashSet<ITypeBinding> bindings = new HashSet<>();
	private ITypeBinding searchedType;
	
	private BindingVisitor(ITypeBinding searchedType) {	
		this.searchedType = searchedType;
	}
	
	public boolean visit(SimpleName node) {
		IBinding binding = node.resolveBinding();
		if (binding instanceof IVariableBinding) {
			IVariableBinding variable = (IVariableBinding) binding;
		    if (!variable.getType().isRawType() && isEqual(variable.getType().getErasure(), searchedType)) {
		    	bindings.add(variable.getType());
		    }
		} else if(binding instanceof IMethodBinding) {
		  	IMethodBinding method = (IMethodBinding) binding;
		  	if (!method.getReturnType().isRawType() && isEqual(method.getReturnType().getErasure(), searchedType)) {
		  		bindings.add(((IMethodBinding) binding).getReturnType());
		  	}
		} else if(binding instanceof ITypeBinding) {
			ITypeBinding type = (ITypeBinding)binding;
		    if (isEqual(type.getErasure(), searchedType)) {
		    	bindings.add(type);
		    }    	
		}
		return super.visit(node);
	}
	
	public static java.util.List<java.util.List<ITypeBinding>> convert(ICompilationUnit unit, ITypeBinding searchedType) {
		BindingVisitor self = new BindingVisitor(searchedType);
		CompilationUnit cUnit = (CompilationUnit) Parser.parse(unit);
		cUnit.accept(self);
		return List.ofAll(self.bindings)
				.map(type -> List.of(type.getTypeArguments()))
		    	.map(List::asJava)
		    	.asJava();
	}
}

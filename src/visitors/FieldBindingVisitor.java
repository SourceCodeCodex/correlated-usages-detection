package visitors;

import java.util.HashSet;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;

import utils.Parser;

public class FieldBindingVisitor extends ASTVisitor {

	private HashSet<IVariableBinding> variableBindings = new HashSet<>();

	public boolean visit(SimpleName node) {
		IBinding binding = node.resolveBinding();
		if (binding instanceof IVariableBinding) {
			IVariableBinding variable = (IVariableBinding) binding;
			if (!variableBindings.contains(variable) && variable.isField()) {
				variableBindings.add(variable);
			}

		}
		return super.visit(node);
	}

	public HashSet<IVariableBinding> getVariableBindings() {
		return variableBindings;
	}

	public static HashSet<IVariableBinding> convert(ICompilationUnit unit) {
		FieldBindingVisitor self = new FieldBindingVisitor();

		CompilationUnit cUnit = Parser.parse(unit);
		cUnit.accept(self);

		return self.getVariableBindings();
	}
}
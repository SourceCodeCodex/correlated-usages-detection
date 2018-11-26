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

public class AttributeBindingVisitor extends ASTVisitor {

	private HashSet<IVariableBinding> attributeBindings = new HashSet<>();

	public boolean visit(SimpleName node) {
		IBinding binding = node.resolveBinding();
		if (binding instanceof IVariableBinding) {
			IVariableBinding variable = (IVariableBinding) binding;
			if (!attributeBindings.contains(variable)) {
				attributeBindings.add(variable);
			}

		}
		return super.visit(node);
	}

	public HashSet<IVariableBinding> getAttributeBindings() {
		return attributeBindings;
	}

	public static HashSet<IVariableBinding> convert(ICompilationUnit unit) {
		AttributeBindingVisitor self = new AttributeBindingVisitor();

		CompilationUnit cUnit = Parser.parse(unit);
		cUnit.accept(self);

		return self.getAttributeBindings();
	}
}
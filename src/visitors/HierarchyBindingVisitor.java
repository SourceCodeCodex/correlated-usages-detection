package visitors;

import java.util.HashSet;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleName;

import utils.Parser;

public class HierarchyBindingVisitor extends ASTVisitor {

	private HashSet<ITypeBinding> typeBindings = new HashSet<>();

	public boolean visit(SimpleName node) {
		IBinding binding = node.resolveBinding();
		if (binding instanceof ITypeBinding) {
			ITypeBinding type = (ITypeBinding) binding;
			if (!typeBindings.contains(type) && !type.isGenericType()) {
				typeBindings.add(type);
			}
		}
		return super.visit(node);
	}

	public HashSet<ITypeBinding> getTypeBindings() {
		return typeBindings;
	}

	public static HashSet<ITypeBinding> convert(ICompilationUnit unit) {
		HierarchyBindingVisitor self = new HierarchyBindingVisitor();

		CompilationUnit cUnit = Parser.parse(unit);
		cUnit.accept(self);

		return self.getTypeBindings();
	}
}
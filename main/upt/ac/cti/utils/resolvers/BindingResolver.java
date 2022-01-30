package upt.ac.cti.utils.resolvers;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

public class BindingResolver {
	private final ASTParser parser;

	private static BindingResolver instance = new BindingResolver();

	private BindingResolver() {
		parser = ASTParser.newParser(AST.getJLSLatest());
		parser.setIgnoreMethodBodies(true);
	}

	public static BindingResolver instance() {
		return instance;
	}

	public ITypeBinding resolveField(IField iField) {
		parser.setProject(iField.getJavaProject());
		IField[] iFields = { iField };
		var bindings = List.of(parser.createBindings(iFields, new NullProgressMonitor()));
		var fieldBindings = bindings.stream().map(v -> ((IVariableBinding) v).getType()).toList();
		return fieldBindings.get(0);
	}

}

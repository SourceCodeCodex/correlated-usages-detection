package upt.se.utils.visitors;

import static upt.se.utils.helpers.LoggerHelper.LOGGER;

import java.util.logging.Level;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import io.vavr.collection.List;
import io.vavr.control.Option;
import upt.se.utils.Parser;

public class IType2ITypeDeclarationBindingConverter extends ASTVisitor {

	private IType searchedType;
	private ITypeBinding typeBinding;
	
	@SuppressWarnings("serial")
	private class TypeFound extends RuntimeException {}

	private IType2ITypeDeclarationBindingConverter(IType searchedType) {
		this.searchedType = searchedType;
	}

	public boolean visit(TypeDeclaration node) {
		IBinding binding = node.resolveBinding();
		if (binding instanceof ITypeBinding && searchedType.equals(binding.getJavaElement())) {
			typeBinding = (ITypeBinding)binding;
			throw new TypeFound();
		}
		return super.visit(node);
	}
	
	public static Option<ITypeBinding> convert(IType type) {
		IType2ITypeDeclarationBindingConverter self = new IType2ITypeDeclarationBindingConverter(type);
	    try {
	    	CompilationUnit cUnit = (CompilationUnit) Parser.parse(type.getCompilationUnit());
	    	cUnit.accept(self);
	    } catch (TypeFound e) {
	    	return Option.of(self.typeBinding);
	    }
	    return Option.none();
	}

	public static final Option<List<ITypeBinding>> convert(List<IType> types) {
		return types.foldLeft(Option.some(List.empty()),
				(res, type) -> convert(type).onEmpty(() -> LOGGER.log(Level.SEVERE,
		            "An error occurred while trying to get all the parameters for: "
		                + type.getFullyQualifiedName()))
		        .map(typeBinding -> res.get().append(typeBinding)));
	}

}

package upt.ac.cti.fields;

import java.util.stream.Stream;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MField;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;

@RelationBuilder
public class SubtypeGroup implements IRelationBuilder<MClass, MField> {

	private static NullProgressMonitor NPM = new NullProgressMonitor();

	@Override
	public Group<MClass> buildGroup(MField mField) {
		var subtypeGroup = new Group<MClass>();
		var iField = (IField) mField.getUnderlyingObject();
		System.out.println(iField.getElementName());
		try {
			var iType = getConcreteType(iField);
			var self = Stream.of(iType);
			var subtypes = Stream.of(iType.newTypeHierarchy(NPM).getAllSubtypes(iType));
			Stream.concat(self, subtypes).map(it -> Factory.getInstance().createMClass(it))
					.forEach(it -> subtypeGroup.add(it));
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return subtypeGroup;
	}

	private IType getConcreteType(IField iField) {
		System.out.println("-----");
		var parser = ASTParser.newParser(AST.JLS16);
		parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
		parser.setSource(iField.getTypeRoot());
		parser.setResolveBindings(true);
		System.out.println("-----");
		var fieldDeclaration = parser.createAST(NPM);
		var iType = fieldDeclaration.ty;
		System.out.println("-----");
		System.out.println(iType.getQualifiedName());
		System.out.println("-----");
		return null;
	}

}
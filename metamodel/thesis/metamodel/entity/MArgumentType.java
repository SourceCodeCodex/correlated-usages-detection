package thesis.metamodel.entity;

public interface MArgumentType extends ro.lrg.xcore.metametamodel.XEntity {

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double apperture();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MArgumentType> unusedArgumentTypes();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MArgumentType> allArgumentTypes();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MArgumentType> usedArgumentTypes();

	org.eclipse.jdt.core.dom.ITypeBinding getUnderlyingObject();
}

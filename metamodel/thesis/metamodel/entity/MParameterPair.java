package thesis.metamodel.entity;

public interface MParameterPair extends ro.lrg.xcore.metametamodel.XEntity {

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double apertureCoverage();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Boolean isBounded();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClassPair> usedArgumentsTypes();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClassPair> unusedArgumentsTypes();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClassPair> allPossibleArgumentsTypes();

	upt.se.utils.ParameterPair getUnderlyingObject();
}

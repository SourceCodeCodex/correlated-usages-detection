package thesis.metamodel.entity;

public interface MTypePair extends ro.lrg.xcore.metametamodel.XEntity {

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MTypePair> actualParameterTypes();

	upt.se.utils.TypePair getUnderlyingObject();
}

package thesis.metamodel.entity;

public interface MTypePair extends ro.lrg.xcore.metametamodel.XEntity {

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString();

	upt.se.utils.TypePair getUnderlyingObject();
}

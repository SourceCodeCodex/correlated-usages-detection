package thesis.metamodel.entity;

public interface MMethod extends ro.lrg.xcore.metametamodel.XEntity {

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer noOfArguments();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString();

	org.eclipse.jdt.core.IMethod getUnderlyingObject();
}

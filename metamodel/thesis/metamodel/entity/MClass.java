package thesis.metamodel.entity;

public interface MClass extends ro.lrg.xcore.metametamodel.XEntity {

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double apertureCoverage();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MParameterPair> typeParameterPairs();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MParameter> typeParameters();

	@ro.lrg.xcore.metametamodel.ThisIsAnAction(numParams = 0) 
	public void showInEditor ();

	org.eclipse.jdt.core.IType getUnderlyingObject();
}

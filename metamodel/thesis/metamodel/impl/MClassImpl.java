package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import upt.se.arguments.OverallIndividualAperture;
import upt.se.arguments.single.ToString;
import upt.se.arguments.OverallPairAperture;
import upt.se.parameters.pair.AllTypeParameterPairs;
import upt.se.parameters.single.AllTypeParameters;
import classes.ShowInEditor;

public class MClassImpl implements MClass {

	private org.eclipse.jdt.core.IType underlyingObj_;

	private static final OverallIndividualAperture OverallIndividualAperture_INSTANCE = new OverallIndividualAperture();
	private static final ToString ToString_INSTANCE = new ToString();
	private static final OverallPairAperture OverallPairAperture_INSTANCE = new OverallPairAperture();
	private static final AllTypeParameterPairs AllTypeParameterPairs_INSTANCE = new AllTypeParameterPairs();
	private static final AllTypeParameters AllTypeParameters_INSTANCE = new AllTypeParameters();
	private static final ShowInEditor ShowInEditor_INSTANCE = new ShowInEditor();

	public MClassImpl(org.eclipse.jdt.core.IType underlyingObj) {
		underlyingObj_ = underlyingObj;
	}

	@Override
	public org.eclipse.jdt.core.IType getUnderlyingObject() {
		return underlyingObj_;
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double overallIndividualAperture() {
		return OverallIndividualAperture_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString() {
		return ToString_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double overallPairAperture() {
		return OverallPairAperture_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MParameterPair> allTypeParameterPairs() {
		return AllTypeParameterPairs_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MParameter> allTypeParameters() {
		return AllTypeParameters_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAnAction
	public void showInEditor() {
		 ShowInEditor_INSTANCE.performAction(this, ro.lrg.xcore.metametamodel.HListEmpty.getInstance());
	}

	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof MClassImpl)) {
			return false;
		}
		MClassImpl iObj = (MClassImpl)obj;
		return underlyingObj_.equals(iObj.underlyingObj_);
	}

	public int hashCode() {
		return 97 * underlyingObj_.hashCode();
	}
}

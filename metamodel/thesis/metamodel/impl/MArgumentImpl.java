package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import upt.se.arguments.single.ToString;
import upt.se.parameters.single.AllTypeParameters;
import upt.se.parameters.pair.AllTypeParameterPairs;
import classes.ShowInEditor;

public class MArgumentImpl implements MArgument {

	private org.eclipse.jdt.core.IType underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();
	private static final AllTypeParameters AllTypeParameters_INSTANCE = new AllTypeParameters();
	private static final AllTypeParameterPairs AllTypeParameterPairs_INSTANCE = new AllTypeParameterPairs();
	private static final ShowInEditor ShowInEditor_INSTANCE = new ShowInEditor();

	public MArgumentImpl(org.eclipse.jdt.core.IType underlyingObj) {
		underlyingObj_ = underlyingObj;
	}

	@Override
	public org.eclipse.jdt.core.IType getUnderlyingObject() {
		return underlyingObj_;
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString() {
		return ToString_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MParameter> allTypeParameters() {
		return AllTypeParameters_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MParameterPair> allTypeParameterPairs() {
		return AllTypeParameterPairs_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAnAction
	public void showInEditor() {
		 ShowInEditor_INSTANCE.performAction(this, ro.lrg.xcore.metametamodel.HListEmpty.getInstance());
	}

	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof MArgumentImpl)) {
			return false;
		}
		MArgumentImpl iObj = (MArgumentImpl)obj;
		return underlyingObj_.equals(iObj.underlyingObj_);
	}

	public int hashCode() {
		return 97 * underlyingObj_.hashCode();
	}
}

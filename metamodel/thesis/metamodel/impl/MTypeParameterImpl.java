package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import upt.se.parameters.ToString;
import upt.se.parameters.Apperture;
import upt.se.parameters.UnusedParameterTypes;
import upt.se.parameters.AllParameterTypes;
import upt.se.parameters.ActualParameterTypes;

public class MTypeParameterImpl implements MTypeParameter {

	private org.eclipse.jdt.core.dom.ITypeBinding underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();
	private static final Apperture Apperture_INSTANCE = new Apperture();
	private static final UnusedParameterTypes UnusedParameterTypes_INSTANCE = new UnusedParameterTypes();
	private static final AllParameterTypes AllParameterTypes_INSTANCE = new AllParameterTypes();
	private static final ActualParameterTypes ActualParameterTypes_INSTANCE = new ActualParameterTypes();

	public MTypeParameterImpl(org.eclipse.jdt.core.dom.ITypeBinding underlyingObj) {
		underlyingObj_ = underlyingObj;
	}

	@Override
	public org.eclipse.jdt.core.dom.ITypeBinding getUnderlyingObject() {
		return underlyingObj_;
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString() {
		return ToString_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double apperture() {
		return Apperture_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> unusedParameterTypes() {
		return UnusedParameterTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MTypeParameter> allParameterTypes() {
		return AllParameterTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MTypeParameter> actualParameterTypes() {
		return ActualParameterTypes_INSTANCE.buildGroup(this);
	}

	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof MTypeParameterImpl)) {
			return false;
		}
		MTypeParameterImpl iObj = (MTypeParameterImpl)obj;
		return underlyingObj_.equals(iObj.underlyingObj_);
	}

	public int hashCode() {
		return 97 * underlyingObj_.hashCode();
	}
}

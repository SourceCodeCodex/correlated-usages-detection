package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import upt.se.parameters.ToString;
import upt.se.parameters.AllSubtypes;
import upt.se.parameters.HierarchyUsages;

public class MTypeParameterImpl implements MTypeParameter {

	private org.eclipse.jdt.core.dom.ITypeBinding underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();
	private static final AllSubtypes AllSubtypes_INSTANCE = new AllSubtypes();
	private static final HierarchyUsages HierarchyUsages_INSTANCE = new HierarchyUsages();

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
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> allSubtypes() {
		return AllSubtypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> hierarchyUsages() {
		return HierarchyUsages_INSTANCE.buildGroup(this);
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

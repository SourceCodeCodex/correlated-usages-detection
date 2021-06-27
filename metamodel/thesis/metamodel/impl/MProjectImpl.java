package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import upt.se.project.ToString;
import upt.se.project.GenericTypesWithLowApertureCoverageBoundedParameterPairs;
import upt.se.project.GenericTypesWithBoundedParameterPairs;
import upt.se.project.GenericTypes;

public class MProjectImpl implements MProject {

	private org.eclipse.jdt.core.IJavaProject underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();
	private static final GenericTypesWithLowApertureCoverageBoundedParameterPairs GenericTypesWithLowApertureCoverageBoundedParameterPairs_INSTANCE = new GenericTypesWithLowApertureCoverageBoundedParameterPairs();
	private static final GenericTypesWithBoundedParameterPairs GenericTypesWithBoundedParameterPairs_INSTANCE = new GenericTypesWithBoundedParameterPairs();
	private static final GenericTypes GenericTypes_INSTANCE = new GenericTypes();

	public MProjectImpl(org.eclipse.jdt.core.IJavaProject underlyingObj) {
		underlyingObj_ = underlyingObj;
	}

	@Override
	public org.eclipse.jdt.core.IJavaProject getUnderlyingObject() {
		return underlyingObj_;
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString() {
		return ToString_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> genericTypesWithLowApertureCoverageBoundedParameterPairs() {
		return GenericTypesWithLowApertureCoverageBoundedParameterPairs_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> genericTypesWithBoundedParameterPairs() {
		return GenericTypesWithBoundedParameterPairs_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> genericTypes() {
		return GenericTypes_INSTANCE.buildGroup(this);
	}

	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof MProjectImpl)) {
			return false;
		}
		MProjectImpl iObj = (MProjectImpl)obj;
		return underlyingObj_.equals(iObj.underlyingObj_);
	}

	public int hashCode() {
		return 97 * underlyingObj_.hashCode();
	}
}

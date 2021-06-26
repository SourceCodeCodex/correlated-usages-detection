package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import upt.se.project.ToString;
import upt.se.project.multiple.unbounded.AllMultipleParameterTypesWithLowAperture;
import upt.se.project.multiple.unbounded.AllMultipleParameterTypes;
import upt.se.project.single.bounded.AllBoundedSingleParameterTypes;
import upt.se.project.GenericTypes;
import upt.se.project.multiple.bounded.AllBoundedMultipleParameterTypesWithLowAperture;
import upt.se.project.single.unbounded.AllSingleParameterTypesWithLowAperture;
import upt.se.project.single.bounded.AllBoundedSingleParameterTypesWithLowAperture;
import upt.se.project.single.unbounded.AllSingleParameterTypes;
import upt.se.project.AllBoundedGenericTypes;
import upt.se.project.multiple.bounded.AllBoundedMultipleParameterTypes;

public class MProjectImpl implements MProject {

	private org.eclipse.jdt.core.IJavaProject underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();
	private static final AllMultipleParameterTypesWithLowAperture AllMultipleParameterTypesWithLowAperture_INSTANCE = new AllMultipleParameterTypesWithLowAperture();
	private static final AllMultipleParameterTypes AllMultipleParameterTypes_INSTANCE = new AllMultipleParameterTypes();
	private static final AllBoundedSingleParameterTypes AllBoundedSingleParameterTypes_INSTANCE = new AllBoundedSingleParameterTypes();
	private static final GenericTypes GenericTypes_INSTANCE = new GenericTypes();
	private static final AllBoundedMultipleParameterTypesWithLowAperture AllBoundedMultipleParameterTypesWithLowAperture_INSTANCE = new AllBoundedMultipleParameterTypesWithLowAperture();
	private static final AllSingleParameterTypesWithLowAperture AllSingleParameterTypesWithLowAperture_INSTANCE = new AllSingleParameterTypesWithLowAperture();
	private static final AllBoundedSingleParameterTypesWithLowAperture AllBoundedSingleParameterTypesWithLowAperture_INSTANCE = new AllBoundedSingleParameterTypesWithLowAperture();
	private static final AllSingleParameterTypes AllSingleParameterTypes_INSTANCE = new AllSingleParameterTypes();
	private static final AllBoundedGenericTypes AllBoundedGenericTypes_INSTANCE = new AllBoundedGenericTypes();
	private static final AllBoundedMultipleParameterTypes AllBoundedMultipleParameterTypes_INSTANCE = new AllBoundedMultipleParameterTypes();

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
	public ro.lrg.xcore.metametamodel.Group<MClass> allMultipleParameterTypesWithLowAperture() {
		return AllMultipleParameterTypesWithLowAperture_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> allMultipleParameterTypes() {
		return AllMultipleParameterTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> allBoundedSingleParameterTypes() {
		return AllBoundedSingleParameterTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> genericTypes() {
		return GenericTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> allBoundedMultipleParameterTypesWithLowAperture() {
		return AllBoundedMultipleParameterTypesWithLowAperture_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> allSingleParameterTypesWithLowAperture() {
		return AllSingleParameterTypesWithLowAperture_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> allBoundedSingleParameterTypesWithLowAperture() {
		return AllBoundedSingleParameterTypesWithLowAperture_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> allSingleParameterTypes() {
		return AllSingleParameterTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> allBoundedGenericTypes() {
		return AllBoundedGenericTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> allBoundedMultipleParameterTypes() {
		return AllBoundedMultipleParameterTypes_INSTANCE.buildGroup(this);
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

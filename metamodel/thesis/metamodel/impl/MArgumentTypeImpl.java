package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import upt.se.arguments.single.ToString;
import upt.se.arguments.single.Apperture;
import upt.se.arguments.single.UnusedArgumentTypes;
import upt.se.arguments.single.AllArgumentTypes;
import upt.se.arguments.single.UsedArgumentTypes;

public class MArgumentTypeImpl implements MArgumentType {

	private org.eclipse.jdt.core.dom.ITypeBinding underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();
	private static final Apperture Apperture_INSTANCE = new Apperture();
	private static final UnusedArgumentTypes UnusedArgumentTypes_INSTANCE = new UnusedArgumentTypes();
	private static final AllArgumentTypes AllArgumentTypes_INSTANCE = new AllArgumentTypes();
	private static final UsedArgumentTypes UsedArgumentTypes_INSTANCE = new UsedArgumentTypes();

	public MArgumentTypeImpl(org.eclipse.jdt.core.dom.ITypeBinding underlyingObj) {
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
	public ro.lrg.xcore.metametamodel.Group<MArgumentType> unusedArgumentTypes() {
		return UnusedArgumentTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MArgumentType> allArgumentTypes() {
		return AllArgumentTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MArgumentType> usedArgumentTypes() {
		return UsedArgumentTypes_INSTANCE.buildGroup(this);
	}

	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof MArgumentTypeImpl)) {
			return false;
		}
		MArgumentTypeImpl iObj = (MArgumentTypeImpl)obj;
		return underlyingObj_.equals(iObj.underlyingObj_);
	}

	public int hashCode() {
		return 97 * underlyingObj_.hashCode();
	}
}

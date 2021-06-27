package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import upt.se.parameters.single.ToString;
import upt.se.parameters.single.UnusedArgumentTypes;
import upt.se.parameters.single.UsedArgumentTypes;
import upt.se.parameters.single.AllPossibleArgumentTypes;

public class MParameterImpl implements MParameter {

	private org.eclipse.jdt.core.dom.ITypeBinding underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();
	private static final UnusedArgumentTypes UnusedArgumentTypes_INSTANCE = new UnusedArgumentTypes();
	private static final UsedArgumentTypes UsedArgumentTypes_INSTANCE = new UsedArgumentTypes();
	private static final AllPossibleArgumentTypes AllPossibleArgumentTypes_INSTANCE = new AllPossibleArgumentTypes();

	public MParameterImpl(org.eclipse.jdt.core.dom.ITypeBinding underlyingObj) {
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
	public ro.lrg.xcore.metametamodel.Group<MClass> unusedArgumentTypes() {
		return UnusedArgumentTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> usedArgumentTypes() {
		return UsedArgumentTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> allPossibleArgumentTypes() {
		return AllPossibleArgumentTypes_INSTANCE.buildGroup(this);
	}

	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof MParameterImpl)) {
			return false;
		}
		MParameterImpl iObj = (MParameterImpl)obj;
		return underlyingObj_.equals(iObj.underlyingObj_);
	}

	public int hashCode() {
		return 97 * underlyingObj_.hashCode();
	}
}

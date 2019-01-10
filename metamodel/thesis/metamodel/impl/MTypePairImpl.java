package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import upt.se.pair.ToString;
import upt.se.pair.Apperture;
import upt.se.pair.AllParameterTypes;
import upt.se.pair.ActualParameterTypes;

public class MTypePairImpl implements MTypePair {

	private upt.se.utils.TypePair underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();
	private static final Apperture Apperture_INSTANCE = new Apperture();
	private static final AllParameterTypes AllParameterTypes_INSTANCE = new AllParameterTypes();
	private static final ActualParameterTypes ActualParameterTypes_INSTANCE = new ActualParameterTypes();

	public MTypePairImpl(upt.se.utils.TypePair underlyingObj) {
		underlyingObj_ = underlyingObj;
	}

	@Override
	public upt.se.utils.TypePair getUnderlyingObject() {
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
	public ro.lrg.xcore.metametamodel.Group<MTypePair> allParameterTypes() {
		return AllParameterTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MTypePair> actualParameterTypes() {
		return ActualParameterTypes_INSTANCE.buildGroup(this);
	}

	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof MTypePairImpl)) {
			return false;
		}
		MTypePairImpl iObj = (MTypePairImpl)obj;
		return underlyingObj_.equals(iObj.underlyingObj_);
	}

	public int hashCode() {
		return 97 * underlyingObj_.hashCode();
	}
}

package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import upt.se.pair.ToString;

public class MTypePairImpl implements MTypePair {

	private upt.se.utils.TypePair underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();

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

package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import upt.se.arguments.pair.ToString;

public class MClassPairImpl implements MClassPair {

	private upt.se.utils.ArgumentPair underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();

	public MClassPairImpl(upt.se.utils.ArgumentPair underlyingObj) {
		underlyingObj_ = underlyingObj;
	}

	@Override
	public upt.se.utils.ArgumentPair getUnderlyingObject() {
		return underlyingObj_;
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString() {
		return ToString_INSTANCE.compute(this);
	}

	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof MClassPairImpl)) {
			return false;
		}
		MClassPairImpl iObj = (MClassPairImpl)obj;
		return underlyingObj_.equals(iObj.underlyingObj_);
	}

	public int hashCode() {
		return 97 * underlyingObj_.hashCode();
	}
}

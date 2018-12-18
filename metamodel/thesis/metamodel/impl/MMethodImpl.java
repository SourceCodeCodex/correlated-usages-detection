package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import methods.NoOfArguments;
import methods.ToString;

public class MMethodImpl implements MMethod {

	private org.eclipse.jdt.core.IMethod underlyingObj_;

	private static final NoOfArguments NoOfArguments_INSTANCE = new NoOfArguments();
	private static final ToString ToString_INSTANCE = new ToString();

	public MMethodImpl(org.eclipse.jdt.core.IMethod underlyingObj) {
		underlyingObj_ = underlyingObj;
	}

	@Override
	public org.eclipse.jdt.core.IMethod getUnderlyingObject() {
		return underlyingObj_;
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer noOfArguments() {
		return NoOfArguments_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString() {
		return ToString_INSTANCE.compute(this);
	}

	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof MMethodImpl)) {
			return false;
		}
		MMethodImpl iObj = (MMethodImpl)obj;
		return underlyingObj_.equals(iObj.underlyingObj_);
	}

	public int hashCode() {
		return 97 * underlyingObj_.hashCode();
	}
}

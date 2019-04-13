package thesis.metamodel.impl;

import thesis.metamodel.entity.*;

public class MTypeParameterImpl implements MTypeParameter {

	private org.eclipse.jdt.core.dom.ITypeBinding underlyingObj_;


	public MTypeParameterImpl(org.eclipse.jdt.core.dom.ITypeBinding underlyingObj) {
		underlyingObj_ = underlyingObj;
	}

	@Override
	public org.eclipse.jdt.core.dom.ITypeBinding getUnderlyingObject() {
		return underlyingObj_;
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

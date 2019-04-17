package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import upt.se.pairs.ToString;
import upt.se.pairs.Apperture;
import upt.se.pairs.UsedArgumentsTypes;
import upt.se.pairs.UnusedArgumentsTypes;
import upt.se.pairs.AllArgumentsTypes;

public class MTypePairImpl implements MTypePair {

	private upt.se.utils.TypePair underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();
	private static final Apperture Apperture_INSTANCE = new Apperture();
	private static final UsedArgumentsTypes UsedArgumentsTypes_INSTANCE = new UsedArgumentsTypes();
	private static final UnusedArgumentsTypes UnusedArgumentsTypes_INSTANCE = new UnusedArgumentsTypes();
	private static final AllArgumentsTypes AllArgumentsTypes_INSTANCE = new AllArgumentsTypes();

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
	public ro.lrg.xcore.metametamodel.Group<MTypePair> usedArgumentsTypes() {
		return UsedArgumentsTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MTypePair> unusedArgumentsTypes() {
		return UnusedArgumentsTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MTypePair> allArgumentsTypes() {
		return AllArgumentsTypes_INSTANCE.buildGroup(this);
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

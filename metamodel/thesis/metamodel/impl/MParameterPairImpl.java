package thesis.metamodel.impl;

import thesis.metamodel.entity.*;
import upt.se.parameters.pair.ToString;
import upt.se.arguments.pair.Apperture;
import upt.se.arguments.pair.UsedArgumentsTypes;
import upt.se.arguments.pair.UnusedArgumentsTypes;
import upt.se.arguments.pair.AllPossibleArgumentsTypes;

public class MParameterPairImpl implements MParameterPair {

	private upt.se.utils.ParameterPair underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();
	private static final Apperture Apperture_INSTANCE = new Apperture();
	private static final UsedArgumentsTypes UsedArgumentsTypes_INSTANCE = new UsedArgumentsTypes();
	private static final UnusedArgumentsTypes UnusedArgumentsTypes_INSTANCE = new UnusedArgumentsTypes();
	private static final AllPossibleArgumentsTypes AllPossibleArgumentsTypes_INSTANCE = new AllPossibleArgumentsTypes();

	public MParameterPairImpl(upt.se.utils.ParameterPair underlyingObj) {
		underlyingObj_ = underlyingObj;
	}

	@Override
	public upt.se.utils.ParameterPair getUnderlyingObject() {
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
	public ro.lrg.xcore.metametamodel.Group<MArgumentPair> usedArgumentsTypes() {
		return UsedArgumentsTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MArgumentPair> unusedArgumentsTypes() {
		return UnusedArgumentsTypes_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MArgumentPair> allPossibleArgumentsTypes() {
		return AllPossibleArgumentsTypes_INSTANCE.buildGroup(this);
	}

	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof MParameterPairImpl)) {
			return false;
		}
		MParameterPairImpl iObj = (MParameterPairImpl)obj;
		return underlyingObj_.equals(iObj.underlyingObj_);
	}

	public int hashCode() {
		return 97 * underlyingObj_.hashCode();
	}
}

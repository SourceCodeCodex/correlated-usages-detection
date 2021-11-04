package upt.ac.cti.model;

import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;

public class TypePair {

	public static TypePair of(Pair<IType, IType> val) {
		return new TypePair(val);
	}

	private final Pair<IType, IType> val;

	private TypePair(Pair<IType, IType> val) {
		this.val = val;
	}

	public Pair<IType, IType> pair() {
		return val;
	}

	@Override
	public String toString() {
		return "(" + val.getValue0().getElementName() + ", " + val.getValue1().getElementName() + ")";
	}

}

package upt.se.utils;

import org.eclipse.jdt.core.dom.ITypeBinding;

public class ParameterPair extends Pair<ITypeBinding, ITypeBinding> {

	public ParameterPair(ITypeBinding _1, ITypeBinding _2) {
		super(_1, _2);
	}
	
	@Override
	public String toString() {
		return "(" + _1.getQualifiedName() + "," + _2.getQualifiedName() + ")";
	}

}

package upt.se.utils.helpers;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class Helper {

	public static boolean isAbstract(IType type) {
		try {
			boolean isAbstract = Flags.isAbstract(type.getFlags());
			return type.isInterface() || isAbstract;
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return false;
	}
}

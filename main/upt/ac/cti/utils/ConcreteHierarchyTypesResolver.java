package upt.ac.cti.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class ConcreteHierarchyTypesResolver {
	private static ConcreteHierarchyTypesResolver instance = new ConcreteHierarchyTypesResolver();

	private ConcreteHierarchyTypesResolver() {

	}

	public static ConcreteHierarchyTypesResolver instance() {
		return instance;
	}

	public List<IType> getConcreteInHierarchyTypes(IType iType) throws JavaModelException {
		var subtypesArray = Arrays.asList(iType.newTypeHierarchy(new NullProgressMonitor()).getAllSubtypes(iType));
		var subtypes = new ArrayList<>(subtypesArray);
		subtypes.add(iType);
		var concrete = subtypes.stream().filter(it -> {
			try {
				return !Flags.isAbstract(it.getFlags());
			} catch (JavaModelException e) {
				e.printStackTrace();
				return false;
			}
		}).toList();
		return concrete;

	}

}

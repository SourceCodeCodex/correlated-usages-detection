package upt.ac.cti.util.hierarchy;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import upt.ac.cti.util.cache.Cache;

public final class ConcreteDescendantsResolver {

  private final Cache<IType, ITypeHierarchy> cache = new Cache<>();

  public List<IType> resolve(IType type) {
    ITypeHierarchy hierarchy;

    var cachedHierarchy = cache.get(type);
    if (cachedHierarchy.isPresent()) {
      hierarchy = cachedHierarchy.get();
    } else {
      try {
        hierarchy = type.newTypeHierarchy(new NullProgressMonitor());
        cache.put(type, hierarchy);
      } catch (JavaModelException e) {
        e.printStackTrace();

        return List.of();
      }
    }

    var subtypes = new ArrayList<>(List.of(hierarchy.getAllSubtypes(type)));
    subtypes.add(type);

    var concrete = subtypes.parallelStream().filter(it -> {
      try {
        var flags = it.getFlags();
        return !(Flags.isAbstract(flags) || Flags.isInterface(flags));
      } catch (JavaModelException e) {
        e.printStackTrace();

        return false;
      }
    }).toList();

    return concrete;
  }

}

package upt.ac.cti.util.hierarchy;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.cache.CacheRegions;

public final class HierarchyResolver {

  private final Cache<IType, ITypeHierarchy> cache = new Cache<>(CacheRegions.HIERARCHY);

  public List<IType> resolveConcrete(IType type) {
    var concrete = resolve(type).parallelStream().filter(it -> {
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

    return subtypes.stream().filter(t -> {
      try {
        return !t.isAnonymous();
      } catch (JavaModelException e) {
        e.printStackTrace();
        return false;
      }
    }).toList();
  }
}

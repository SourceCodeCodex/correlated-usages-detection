package upt.ac.cti.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import upt.ac.cti.cache.Cache;

public final class HierarchyResolver {

  private static final Cache<IType, ITypeHierarchy> cache = new Cache<>();

  private static final Logger logger = Logger.getLogger(HierarchyResolver.class.getName());

  public List<IType> resolveConcreteDescendets(IType type) {
    ITypeHierarchy hierarchy;

    var cachedHierarchy = cache.get(type);
    if (cachedHierarchy.isPresent()) {
      hierarchy = cachedHierarchy.get();
    } else {
      try {
        hierarchy = type.newTypeHierarchy(new NullProgressMonitor());
        cache.put(type, hierarchy);
      } catch (JavaModelException e) {
        var ste = e.getStackTrace()[0];
        logger.throwing(ste.getClassName(), ste.getMethodName(), e);

        return List.of();
      }
    }

    var subtypes = new ArrayList<>(List.of(hierarchy.getAllSubtypes(type)));
    subtypes.add(type);

    var concrete = subtypes.stream().filter(it -> {
      try {
        return !Flags.isAbstract(it.getFlags());
      } catch (JavaModelException e) {
        var ste = e.getStackTrace()[0];
        logger.throwing(ste.getClassName(), ste.getMethodName(), e);

        return false;
      }
    }).toList();

    return concrete;
  }

}

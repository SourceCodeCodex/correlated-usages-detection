package upt.ac.cti.coverage.derivator.util;

import java.util.logging.Logger;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.binding.NotLeafBinding;
import upt.ac.cti.coverage.model.binding.ResolvedBinding;
import upt.ac.cti.coverage.model.binding.WritingBinding;
import upt.ac.cti.util.binding.ABindingResolver;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.hierarchy.HierarchyResolver;
import upt.ac.cti.util.logging.RLogger;

public final class WritingBindingResolver<J extends IJavaElement> {

  private final Cache<Writing<J>, WritingBinding> cache =
      new Cache<>();

  private static final Logger logger = RLogger.get();

  private final HierarchyResolver hierarchyResolver;
  private final ABindingResolver<J, ITypeBinding> aBindingResolver;

  public WritingBindingResolver(HierarchyResolver hierarchyResolver,
      ABindingResolver<J, ITypeBinding> aBindingResolver) {
    this.hierarchyResolver = hierarchyResolver;
    this.aBindingResolver = aBindingResolver;
  }

  public WritingBinding resolveBinding(Writing<J> writing) {
    var cached = cache.get(writing);
    if (cached.isPresent()) {
      return cached.get();
    }

    if (writing.writingExpression().getNodeType() == ASTNode.NULL_LITERAL) {
      cache.put(writing, WritingBinding.INCONCLUSIVE);
      return WritingBinding.INCONCLUSIVE;
    }

    var writingExpressionBinding = writing.writingExpression().resolveTypeBinding();
    if (writingExpressionBinding == null) {
      logger
          .warning("Could not resolve ITypeBinding for writing expression: " + writing);
      cache.put(writing, WritingBinding.INCONCLUSIVE);
      return WritingBinding.INCONCLUSIVE;
    }

    if (writingExpressionBinding.isArray() || !writingExpressionBinding.isFromSource()) {
      cache.put(writing, WritingBinding.INCONCLUSIVE);
      return WritingBinding.INCONCLUSIVE;
    }

    var type = (IType) writingExpressionBinding.getJavaElement();
    if (type == null) {
      logger.warning("Could not resolve IType for field writing expression: " + writing);
      cache.put(writing, WritingBinding.INCONCLUSIVE);
      return WritingBinding.INCONCLUSIVE;
    }

    if (writing.writingExpression().getNodeType() == ASTNode.CLASS_INSTANCE_CREATION) {
      var resolved = new ResolvedBinding(writingExpressionBinding);
      cache.put(writing, resolved);
      return resolved;
    }

    if (writing.writingExpression().getNodeType() == ASTNode.THIS_EXPRESSION) {
      try {
        var flags = type.getFlags();
        if (Flags.isAbstract(flags) || Flags.isInterface(flags)) {
          return WritingBinding.INCONCLUSIVE;
        }

        var resolved = new ResolvedBinding(writingExpressionBinding);
        cache.put(writing, resolved);
        return resolved;
      } catch (JavaModelException e) {
        e.printStackTrace();
        return WritingBinding.INCONCLUSIVE;
      }
    }


    var hierarchy = hierarchyResolver.resolveConcrete(type);
    if (hierarchy.size() == 1 && hierarchy.contains(type)) {
      var resolved = new ResolvedBinding(writingExpressionBinding);
      cache.put(writing, resolved);
      return resolved;
    }

    var jBinding = aBindingResolver.resolve(writing.element());
    if (jBinding.isPresent()) {
      var jType = (IType) jBinding.get().getJavaElement();
      hierarchy = hierarchyResolver.resolve(jType);
      if (hierarchy.contains(type)) {
        var notALeaf = new NotLeafBinding(writingExpressionBinding);
        cache.put(writing, notALeaf);
        return notALeaf;
      }
    }

    logger.warning("Writing's binding is inconclusive: " + writing);
    cache.put(writing, WritingBinding.INCONCLUSIVE);
    return WritingBinding.INCONCLUSIVE;
  }

}

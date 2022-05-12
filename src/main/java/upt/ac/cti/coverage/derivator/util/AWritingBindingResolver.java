package upt.ac.cti.coverage.derivator.util;

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
import upt.ac.cti.util.cache.CacheRegions;
import upt.ac.cti.util.hierarchy.HierarchyResolver;
import upt.ac.cti.util.logging.RLogger;

public abstract class AWritingBindingResolver<J extends IJavaElement> {

  private final Cache<Writing<? extends IJavaElement>, WritingBinding> cache =
      new Cache<>(CacheRegions.W_BINDING);

  private final HierarchyResolver hierarchyResolver;
  private final ABindingResolver<J, ITypeBinding> aBindingResolver;

  public AWritingBindingResolver(HierarchyResolver hierarchyResolver,
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

    if (writing.writingExpression().getNodeType() == ASTNode.ARRAY_ACCESS) {
      cache.put(writing, WritingBinding.INCONCLUSIVE);
      return WritingBinding.INCONCLUSIVE;
    }

    var writingExpressionBinding = writing.writingExpression().resolveTypeBinding();
    if (writingExpressionBinding == null) {
      cache.put(writing, WritingBinding.INCONCLUSIVE);
      return WritingBinding.INCONCLUSIVE;
    }

    if (writingExpressionBinding.isArray() || !writingExpressionBinding.isFromSource()) {
      cache.put(writing, WritingBinding.INCONCLUSIVE);
      return WritingBinding.INCONCLUSIVE;
    }

    var javaEl = writingExpressionBinding.getJavaElement();
    if (javaEl == null || !(javaEl instanceof IType)) {
      cache.put(writing, WritingBinding.INCONCLUSIVE);
      return WritingBinding.INCONCLUSIVE;
    }

    var expressionType = (IType) writingExpressionBinding.getJavaElement();

    if (writing.writingExpression().getNodeType() == ASTNode.CLASS_INSTANCE_CREATION) {
      var resolved = new ResolvedBinding(writingExpressionBinding);
      cache.put(writing, resolved);
      return resolved;
    }

    if (writing.writingExpression().getNodeType() == ASTNode.THIS_EXPRESSION) {
      try {
        var flags = expressionType.getFlags();
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

    var expressionHierarchy = hierarchyResolver.resolveConcrete(expressionType);
    if (expressionHierarchy.size() == 1 && expressionHierarchy.contains(expressionType)) {
      var resolved = new ResolvedBinding(writingExpressionBinding);
      cache.put(writing, resolved);
      return resolved;
    }

    var fieldBinding = aBindingResolver.resolve(writing.element());
    if (fieldBinding.isPresent()) {
      var fieldType = (IType) fieldBinding.get().getJavaElement();
      var fieldHierarchy = hierarchyResolver.resolve(fieldType);
      expressionHierarchy = hierarchyResolver.resolve(expressionType);
      if (fieldHierarchy.contains(expressionType) || expressionHierarchy.contains(fieldType)) {
        var notALeaf = new NotLeafBinding(writingExpressionBinding);
        cache.put(writing, notALeaf);
        return notALeaf;
      }
    }

    RLogger.get().warning("Writing binding inconclusive for unknown reason: " + writing);
    cache.put(writing, WritingBinding.INCONCLUSIVE);
    return WritingBinding.INCONCLUSIVE;
  }

}

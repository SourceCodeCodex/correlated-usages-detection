package upt.ac.cti.coverage.flow_insensitive.derivator.util;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.binding.NotLeafBinding;
import upt.ac.cti.coverage.flow_insensitive.model.binding.ResolvedBinding;
import upt.ac.cti.coverage.flow_insensitive.model.binding.WritingBinding;
import upt.ac.cti.util.binding.ABindingResolver;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.cache.CacheRegions;
import upt.ac.cti.util.hierarchy.HierarchyResolver;
import upt.ac.cti.util.validation.IsTypeBindingCollection;

public abstract class AWritingBindingResolver<J extends IJavaElement> {

  private final Cache<Writing<? extends IJavaElement>, WritingBinding> cache =
      new Cache<>(CacheRegions.W_BINDING);

  private final HierarchyResolver hierarchyResolver;
  private final ABindingResolver<J, ITypeBinding> aBindingResolver;

  private static final IsTypeBindingCollection isTBCollection = new IsTypeBindingCollection();

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

    if (writingExpressionBinding.isArray() || isTBCollection.test(writingExpressionBinding)
        || !writingExpressionBinding.isFromSource()) {
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
      var resolved = new ResolvedBinding(writingExpressionBinding);
      cache.put(writing, resolved);
      return resolved;

    }

    var expressionHierarchy = hierarchyResolver.resolveConcrete(expressionType);
    if (expressionHierarchy.size() == 1 && expressionHierarchy.contains(expressionType)) {
      var resolved = new ResolvedBinding(writingExpressionBinding);
      cache.put(writing, resolved);
      return resolved;
    }

    var fieldBindingOpt = aBindingResolver.resolve(writing.element());
    if (fieldBindingOpt.isPresent()) {
      var fieldBinding = fieldBindingOpt.get();
      if (isTBCollection.test(fieldBinding)) {
        fieldBinding = fieldBinding.getTypeArguments()[0];
        if (fieldBinding.getJavaElement() == null
            || !(fieldBinding.getJavaElement() instanceof IType)) {
          cache.put(writing, WritingBinding.INCONCLUSIVE);
          return WritingBinding.INCONCLUSIVE;
        }
      }
    }

    var notALeaf = new NotLeafBinding(writingExpressionBinding);
    cache.put(writing, notALeaf);
    return notALeaf;
  }

}

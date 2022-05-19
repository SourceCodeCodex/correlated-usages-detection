package upt.ac.cti.coverage.flow_insensitive.derivator.util;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;
import upt.ac.cti.coverage.flow_insensitive.model.binding.NotLeafBinding;
import upt.ac.cti.coverage.flow_insensitive.model.binding.ResolvedBinding;
import upt.ac.cti.coverage.flow_insensitive.model.binding.WritingBinding;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.binding.ABindingResolver;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.cache.CacheRegion;
import upt.ac.cti.util.hierarchy.HierarchyResolver;
import upt.ac.cti.util.validation.IsTypeBindingCollection;

public abstract class ADerivableWritingBindingResolver<J extends IJavaElement> {

  private final Cache<DerivableWriting<? extends IJavaElement>, WritingBinding> cache =
      new Cache<>(CacheRegion.W_BINDING);

  private final HierarchyResolver hierarchyResolver = Dependencies.hierarchyResolver;
  private final IsTypeBindingCollection isTBCollection = Dependencies.isTypeBindingCollection;

  private final ABindingResolver<J, ITypeBinding> aBindingResolver;

  public ADerivableWritingBindingResolver(ABindingResolver<J, ITypeBinding> aBindingResolver) {
    this.aBindingResolver = aBindingResolver;
  }

  public WritingBinding resolveBinding(DerivableWriting<J> writing) {
    var cached = cache.get(writing);
    if (cached.isPresent()) {
      return cached.get();
    }

    var writingExpression = writing.writingExpression();

    if (writingExpression.getNodeType() == ASTNode.NULL_LITERAL) {
      cache.put(writing, WritingBinding.INCONCLUSIVE);
      return WritingBinding.INCONCLUSIVE;
    }

    if (writingExpression.getNodeType() == ASTNode.ARRAY_ACCESS) {
      cache.put(writing, WritingBinding.INCONCLUSIVE);
      return WritingBinding.INCONCLUSIVE;
    }

    var writingExpressionBinding = writingExpression.resolveTypeBinding();

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

    if (writingExpression.getNodeType() == ASTNode.CLASS_INSTANCE_CREATION) {
      var resolved = new ResolvedBinding(writingExpressionBinding);
      cache.put(writing, resolved);
      return resolved;
    }

    if (writingExpression.getNodeType() == ASTNode.THIS_EXPRESSION) {
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

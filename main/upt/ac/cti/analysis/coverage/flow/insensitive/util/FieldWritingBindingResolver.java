package upt.ac.cti.analysis.coverage.flow.insensitive.util;

import java.util.Optional;
import java.util.logging.Logger;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.cache.Cache;
import upt.ac.cti.util.HierarchyResolver;

public final class FieldWritingBindingResolver {

  private static final Cache<FieldWriting, ITypeBinding> cache =
      new Cache<>();

  private static final Logger logger =
      Logger.getLogger(FieldWritingBindingResolver.class.getName());

  private final HierarchyResolver hierarchyResolver;

  public FieldWritingBindingResolver(HierarchyResolver hierarchyResolver) {
    this.hierarchyResolver = hierarchyResolver;
  }

  public Optional<ITypeBinding> resolveBinding(FieldWriting fieldWriting) {
    var cachedBinding = cache.get(fieldWriting);
    if (cachedBinding.isPresent()) {
      return cachedBinding;
    }

    var writingExpressionBinding = fieldWriting.writingExpression().resolveTypeBinding();

    if (writingExpressionBinding == null) {
      logger
          .warning("Could not resolve ITypeBinding for field writing expression: " + fieldWriting);
      return Optional.empty();
    }

    if (fieldWriting.writingExpression().getNodeType() == ASTNode.CLASS_INSTANCE_CREATION) {
      cache.put(fieldWriting, writingExpressionBinding);
      return Optional.of(writingExpressionBinding);
    }

    if (fieldWriting.writingExpression().getNodeType() == ASTNode.NULL_LITERAL) {
      return Optional.empty();
    }

    var type = (IType) writingExpressionBinding.getJavaElement();
    if (type == null) {
      logger.warning("Could not resolve IType for field writing expression: " + fieldWriting);
      return Optional.empty();
    }
    if (hierarchyResolver
        .resolveConcreteDescendets(type)
        .size() == 1) {
      cache.put(fieldWriting, writingExpressionBinding);
      return Optional.of(writingExpressionBinding);
    }
    return Optional.empty();
  }

}

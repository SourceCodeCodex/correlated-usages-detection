package upt.ac.cti.coverage.util;

import java.util.Optional;
import java.util.logging.Logger;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;
import upt.ac.cti.util.logging.RLogger;

public final class FieldWritingBindingResolver {

  private final Cache<Writing, ITypeBinding> cache =
      new Cache<>();

  private static final Logger logger = RLogger.get();

  private final ConcreteDescendantsResolver hierarchyResolver;

  public FieldWritingBindingResolver(ConcreteDescendantsResolver hierarchyResolver) {
    this.hierarchyResolver = hierarchyResolver;
  }

  public Optional<ITypeBinding> resolveBinding(Writing fieldWriting) {
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
        .resolve(type)
        .size() == 1) {
      cache.put(fieldWriting, writingExpressionBinding);
      return Optional.of(writingExpressionBinding);
    }
    return Optional.empty();
  }

}

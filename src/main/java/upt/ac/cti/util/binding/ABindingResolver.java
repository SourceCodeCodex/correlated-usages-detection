package upt.ac.cti.util.binding;

import java.util.Optional;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.IBinding;
import upt.ac.cti.util.binding.visitor.ABindingResolverVisitor;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.cache.CacheRegion;

public abstract class ABindingResolver<J extends IJavaElement, B extends IBinding> {

  private final Cache<ICompilationUnit, ASTNode> astCache = new Cache<>(CacheRegion.BINDING);

  public abstract Optional<B> resolve(J member);

  protected Optional<B> resolve(J member,
      IJavaProject project,
      ICompilationUnit compilationUnit,
      ABindingResolverVisitor<J, B> visitor) {

    if (project == null || compilationUnit == null) {
      return Optional.empty();
    }

    ASTNode cuAST;

    var cachedCuAST = astCache.get(compilationUnit);
    if (cachedCuAST.isPresent()) {
      cuAST = cachedCuAST.get();
    } else {
      var parser = ASTParser.newParser(AST.getJLSLatest());

      parser.setSource(compilationUnit);
      parser.setProject(project);
      parser.setKind(ASTParser.K_COMPILATION_UNIT);
      parser.setResolveBindings(true);
      parser.setIgnoreMethodBodies(true);

      cuAST = parser.createAST(new NullProgressMonitor());
      astCache.put(compilationUnit, cuAST);
    }

    cuAST.accept(visitor);
    return visitor.binding();
  }
}

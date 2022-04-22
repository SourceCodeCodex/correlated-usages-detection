package upt.ac.cti.analysis.coverage.flow.insensitive.parser;

import java.util.Optional;
import java.util.logging.Logger;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import upt.ac.cti.cache.Cache;


public final class CodeParser {

  private static final Cache<IMethod, MethodDeclaration> methodCache = new Cache<>();
  private static final Cache<ICompilationUnit, CompilationUnit> compilationUnitCache =
      new Cache<>();

  private static final Logger logger = Logger.getLogger(CodeParser.class.getName());

  public CompilationUnit parse(ICompilationUnit compilationUnit) {
    var cachedCU = compilationUnitCache.get(compilationUnit);
    if (cachedCU.isPresent()) {
      return cachedCU.get();
    }
    var parser = ASTParser.newParser(AST.getJLSLatest());
    parser.setSource(compilationUnit);
    parser.setProject(compilationUnit.getJavaProject());
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setResolveBindings(true);

    var cu = (CompilationUnit) parser.createAST(new NullProgressMonitor());

    compilationUnitCache.put(compilationUnit, cu);
    return cu;
  }

  public Optional<MethodDeclaration> parse(IMethod method) {
    var cachedMethodDeclaration = methodCache.get(method);
    if (cachedMethodDeclaration.isPresent()) {
      return cachedMethodDeclaration;
    }

    var compilationUnit = method.getCompilationUnit();

    if (compilationUnit == null) {
      logger.warning("Could not parse method " + method.getElementName()
          + " as it's compilation unit could not be found!");
      return Optional.empty();
    }

    var cuAST = parse(compilationUnit);

    var visitor = new ASTVisitor() {
      private MethodDeclaration result;

      @Override
      public boolean visit(MethodDeclaration node) {
        if (method.equals(node.resolveBinding().getJavaElement())) {
          methodCache.put(method, node);
          result = node;
        }
        return true;
      }
    };
    cuAST.accept(visitor);

    methodCache.put(method, visitor.result);
    return Optional.of(visitor.result);
  }

}

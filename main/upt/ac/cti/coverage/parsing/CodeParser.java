package upt.ac.cti.coverage.parsing;

import java.util.Optional;
import java.util.logging.Logger;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.logging.RLogger;


public final class CodeParser {

  private final Cache<IJavaElement, ASTNode> cache = new Cache<>();

  private static final Logger logger = RLogger.get();

  public Optional<CompilationUnit> parse(ICompilationUnit compilationUnit) {
    var cachedCU = cache.get(compilationUnit);
    if (cachedCU.isPresent()) {
      return cachedCU.map(cu -> (CompilationUnit) cu);
    }

    if (compilationUnit.getJavaProject() == null) {
      warnCouldNotParse(compilationUnit, "Java Project is null.");
      return Optional.empty();
    }

    var parser = ASTParser.newParser(AST.getJLSLatest());
    parser.setSource(compilationUnit);
    parser.setProject(compilationUnit.getJavaProject());
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setResolveBindings(true);

    var cu = (CompilationUnit) parser.createAST(new NullProgressMonitor());

    cache.put(compilationUnit, cu);
    return Optional.of(cu);
  }

  public Optional<MethodDeclaration> parse(IMethod method) {
    var cachedMethodDeclaration = cache.get(method);
    if (cachedMethodDeclaration.isPresent()) {
      return cachedMethodDeclaration.map(md -> (MethodDeclaration) md);
    }

    var compilationUnit = method.getCompilationUnit();

    if (compilationUnit == null) {
      warnCompilationUnitNull(method);
      return Optional.empty();
    }

    var cuASTOpt = parse(compilationUnit);

    var visitor = new ASTVisitor() {
      private Optional<MethodDeclaration> result = Optional.empty();

      @Override
      public boolean visit(MethodDeclaration node) {
        if (method.equals(node.resolveBinding().getJavaElement())) {
          result = Optional.of(node);
          cache.put(method, node);
          return false;
        }
        return true;

      }
    };

    return cuASTOpt.flatMap(cu -> {
      cu.accept(visitor);
      return visitor.result;
    });
  }

  public Optional<VariableDeclarationFragment> parse(IField field) {
    var cachedFieldDeclaration = cache.get(field);
    if (cachedFieldDeclaration.isPresent()) {
      return cachedFieldDeclaration.map(fd -> (VariableDeclarationFragment) fd);
    }

    var compilationUnit = field.getCompilationUnit();

    if (compilationUnit == null) {
      warnCompilationUnitNull(field);
      return Optional.empty();
    }

    var cuASTOpt = parse(compilationUnit);

    var visitor = new ASTVisitor() {
      private Optional<VariableDeclarationFragment> result = Optional.empty();

      @Override
      public boolean visit(VariableDeclarationFragment node) {
        if (field.equals(node.resolveBinding().getJavaElement())) {
          result = Optional.of(node);
          cache.put(field, node);
          return false;
        }

        return true;

      }
    };

    return cuASTOpt.flatMap(cu -> {
      cu.accept(visitor);
      return visitor.result;
    });

  }

  private void warnCompilationUnitNull(IJavaElement element) {
    warnCouldNotParse(element, "Compilation unit is null.");
  }

  private void warnCouldNotParse(IJavaElement element, String reason) {
    var log = String.format("Could not parse: %s. Type: %d. Reason: %s",
        element.getElementName(), element.getElementType(), reason);
    logger.warning(log);
  }

}

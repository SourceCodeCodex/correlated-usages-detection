package upt.ac.cti.analysis.coverage.flow.insensitive.parser;

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


public final class CodeParser {

  private final Cache<IJavaElement, ASTNode> cache = new Cache<>();

  private static final Logger logger = Logger.getLogger(CodeParser.class.getName());

  public CompilationUnit parse(ICompilationUnit compilationUnit) {
    var cachedCU = cache.get(compilationUnit);
    if (cachedCU.isPresent()) {
      return (CompilationUnit) cachedCU.get();
    }
    var parser = ASTParser.newParser(AST.getJLSLatest());
    parser.setSource(compilationUnit);
    parser.setProject(compilationUnit.getJavaProject());
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setResolveBindings(true);

    var cu = (CompilationUnit) parser.createAST(new NullProgressMonitor());

    cache.put(compilationUnit, cu);
    return cu;
  }

  public Optional<MethodDeclaration> parse(IMethod method) {
    var cachedMethodDeclaration = cache.get(method);
    if (cachedMethodDeclaration.isPresent()) {
      return cachedMethodDeclaration.map(md -> (MethodDeclaration) md);
    }

    var compilationUnit = method.getCompilationUnit();

    if (compilationUnit == null) {
      logger.warning("Could not parse method " + method.getElementName()
          + " as it's compilation unit could not be found!");
      return Optional.empty();
    }

    var cuAST = parse(compilationUnit);

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
    cuAST.accept(visitor);
    return visitor.result;
  }

  public Optional<VariableDeclarationFragment> parse(IField field) {
    var cachedFieldDeclaration = cache.get(field);
    if (cachedFieldDeclaration.isPresent()) {
      return cachedFieldDeclaration.map(fd -> (VariableDeclarationFragment) fd);
    }

    var compilationUnit = field.getCompilationUnit();

    if (compilationUnit == null) {
      logger.warning("Could not parse fileld " + field.getElementName()
          + " as it's compilation unit could not be found!");
      return Optional.empty();
    }

    var cuAST = parse(compilationUnit);

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
    cuAST.accept(visitor);

    return visitor.result;

  }


}

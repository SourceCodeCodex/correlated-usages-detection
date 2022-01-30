package upt.ac.cti.utils.parsers;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;


public class CachedParser {
  private final ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
  private static CachedParser instance = new CachedParser();

  private final Map<IMethod, MethodDeclaration> methodsCache = new HashMap<>();
  private final Map<ICompilationUnit, CompilationUnit> compilationUnitsCache = new HashMap<>();

  public static CachedParser instance() {
    return instance;
  }

  public void refresh() {
    methodsCache.clear();
    compilationUnitsCache.clear();
  }

  public ASTNode parse(ICompilationUnit iCompilationUnit) {
    if (compilationUnitsCache.containsKey(iCompilationUnit)) {
      return compilationUnitsCache.get(iCompilationUnit);
    }
    parser.setSource(iCompilationUnit);
    parser.setProject(iCompilationUnit.getJavaProject());
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setResolveBindings(true);
    var astNode = (CompilationUnit) parser.createAST(new NullProgressMonitor());
    compilationUnitsCache.put(iCompilationUnit, astNode);
    return astNode;
  }

  public MethodDeclaration parse(IMethod iMethod) {
    if (methodsCache.containsKey(iMethod)) {
      return methodsCache.get(iMethod);
    }
    var cuAST = parse(iMethod.getCompilationUnit());
    var visitor = new ASTVisitor() {
      private MethodDeclaration result;

      @Override
      public boolean visit(MethodDeclaration node) {
        if (iMethod.equals(node.resolveBinding().getJavaElement())) {
          methodsCache.put(iMethod, node);
          result = node;
        }
        return true;
      }
    };
    cuAST.accept(visitor);
    return visitor.result;
  }

}

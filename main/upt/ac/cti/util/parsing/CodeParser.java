package upt.ac.cti.util.parsing;

import java.util.Optional;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.parsing.visitor.AASTNodeResolverVisitor;
import upt.ac.cti.util.parsing.visitor.MethodDeclarationResolverVisitor;
import upt.ac.cti.util.parsing.visitor.VariableDeclarationFragmentResolverVisitor;


public final class CodeParser {

  private final Cache<IJavaElement, ASTNode> cache = new Cache<>();

  public Optional<CompilationUnit> parse(ICompilationUnit compilationUnit) {
    var cachedCU = cache.get(compilationUnit);
    if (cachedCU.isPresent()) {
      return cachedCU.map(cu -> (CompilationUnit) cu);
    }

    if (compilationUnit.getJavaProject() == null) {
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
    var visitor = new MethodDeclarationResolverVisitor(method);
    return parseMember(method, visitor);
  }

  public Optional<VariableDeclarationFragment> parse(IField field) {
    var visitor = new VariableDeclarationFragmentResolverVisitor(field);
    return parseMember(field, visitor);
  }

  @SuppressWarnings("unchecked")
  private <T extends ASTNode> Optional<T> parseMember(
      IMember member,
      AASTNodeResolverVisitor<? extends IMember, T> visitor) {
    var cached = cache.get(member);
    if (cached.isPresent()) {
      return cached.map(node -> (T) node);
    }

    var compilationUnit = member.getCompilationUnit();

    if (compilationUnit == null) {
      return Optional.empty();
    }

    var cuASTOpt = parse(compilationUnit);

    var nodeOpt = cuASTOpt.flatMap(cu -> {
      cu.accept(visitor);
      return visitor.node();
    });

    if (nodeOpt.isPresent()) {
      cache.put(member, nodeOpt.get());
    }

    return nodeOpt;
  }

}

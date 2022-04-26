package upt.ac.cti.model.util;

import java.util.List;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import upt.ac.cti.util.cache.Cache;

public final class FieldBindingResolver {

  private final Cache<IField, ITypeBinding> cache = new Cache<>();

  public ITypeBinding resolve(IField field) {
    var cachedBinding = cache.get(field);
    if (cachedBinding.isPresent()) {
      return cachedBinding.get();
    }

    IField[] iFields = {field};

    var parser = ASTParser.newParser(AST.getJLSLatest());
    parser.setIgnoreMethodBodies(true);
    parser.setProject(field.getJavaProject());
    var bindings = List.of(parser.createBindings(iFields, new NullProgressMonitor()));

    var fieldBindings = bindings.stream()
        .map(v -> ((IVariableBinding) v).getType())
        .toList();


    cache.put(field, fieldBindings.get(0));
    return fieldBindings.get(0);
  }

}

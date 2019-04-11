package upt.se.utils.visitors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;

import upt.se.utils.Parser;

public class VariableBindingVisitor extends ASTVisitor {
    
    private static Map<ICompilationUnit, HashSet<IVariableBinding>> allVariableBindings = new HashMap<>();
    private HashSet<IVariableBinding>                               attributeBindings   = new HashSet<>();
    
    public boolean visit(SimpleName node) {
        IBinding binding = node.resolveBinding();
        if (binding instanceof IVariableBinding) {
            IVariableBinding variable = (IVariableBinding) binding;
            if (!attributeBindings.contains(variable)) {
                attributeBindings.add(variable);
            }
        }
        return super.visit(node);
    }
    
    public HashSet<IVariableBinding> getAttributeBindings() {
        return attributeBindings;
    }
    
    public static HashSet<IVariableBinding> convert(ICompilationUnit unit) {
        if (allVariableBindings.containsKey(unit)) { return allVariableBindings.get(unit); }
        
        VariableBindingVisitor self = new VariableBindingVisitor();
        
        CompilationUnit cUnit = (CompilationUnit) Parser.parse(unit);
        cUnit.accept(self);
        
        allVariableBindings.put(unit, self.getAttributeBindings());
        return allVariableBindings.get(unit);
    }
}
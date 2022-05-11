package upt.ac.cti.coverage.model.binding;

import org.eclipse.jdt.core.dom.ITypeBinding;

public final record ResolvedBinding(ITypeBinding typeBinding)
    implements WritingBinding {
}

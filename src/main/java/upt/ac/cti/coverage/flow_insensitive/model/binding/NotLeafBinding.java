package upt.ac.cti.coverage.flow_insensitive.model.binding;

import org.eclipse.jdt.core.dom.ITypeBinding;

public final record NotLeafBinding(ITypeBinding typeBinding)
    implements WritingBinding {
}

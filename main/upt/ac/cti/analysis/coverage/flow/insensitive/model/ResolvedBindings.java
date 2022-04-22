package upt.ac.cti.analysis.coverage.flow.insensitive.model;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;

public final record ResolvedBindings(Pair<ITypeBinding, ITypeBinding> bindingsPair)
    implements DerivationResult {
}

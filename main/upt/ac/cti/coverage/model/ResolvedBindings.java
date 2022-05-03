package upt.ac.cti.coverage.model;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;

public final record ResolvedBindings(Pair<ITypeBinding, ITypeBinding> bindingsPair)
    implements DerivationResult {
}

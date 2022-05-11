package upt.ac.cti.coverage.model.derivation;

import org.eclipse.jdt.core.IJavaElement;

public sealed interface DerivationResult<J extends IJavaElement> permits NewWritingPairs<J>, ResolvedTypePairs<J>, Inconclusive<J> {

}

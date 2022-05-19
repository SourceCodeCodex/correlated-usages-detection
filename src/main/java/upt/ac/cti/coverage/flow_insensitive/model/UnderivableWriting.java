package upt.ac.cti.coverage.flow_insensitive.model;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.Expression;

public final record UnderivableWriting<T extends IJavaElement> (
    T element,
    Expression writingExpression,
    int depth) implements Writing<T> {

}

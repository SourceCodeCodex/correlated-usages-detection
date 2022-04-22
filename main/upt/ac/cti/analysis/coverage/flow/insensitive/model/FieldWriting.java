package upt.ac.cti.analysis.coverage.flow.insensitive.model;

import java.util.Optional;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.Expression;

public final record FieldWriting(IField field, Expression writingExpression,
    Optional<Expression> accessExpression) {

  public FieldWriting withWritingExpression(Expression writingExpression) {
    return new FieldWriting(field, writingExpression, accessExpression);
  }

  public FieldWriting withBaseObject(Expression accessExpression) {
    return new FieldWriting(field, writingExpression, Optional.of(accessExpression));
  }

}

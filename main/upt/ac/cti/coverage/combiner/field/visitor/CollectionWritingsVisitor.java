package upt.ac.cti.coverage.combiner.field.visitor;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodInvocation;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.util.Either;

public class CollectionWritingsVisitor extends AFieldWritingsVisitor {

  public CollectionWritingsVisitor(IField field) {
    super(field);
  }

  @Override
  public boolean visit(MethodInvocation node) {
    var expression = node.getExpression();
    if (expression instanceof FieldAccess fa
        && fa.resolveFieldBinding() != null
        && field.equals(fa.resolveFieldBinding().getJavaElement())) {
      switch (node.getName().getIdentifier()) {
        case "add": {
          var fieldWrite =
              new Writing<>(field, (Expression) node.arguments().get(0),
                  Either.left(field.getDeclaringType()));
          result.add(fieldWrite);
        }
      }
    }
    return false;
  }


}

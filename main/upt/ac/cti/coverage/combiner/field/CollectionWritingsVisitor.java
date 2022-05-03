package upt.ac.cti.coverage.combiner.field;

import java.util.Optional;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodInvocation;
import upt.ac.cti.coverage.model.Writing;

class CollectionWritingsVisitor extends AFieldWritingsVisitor {

  public CollectionWritingsVisitor(IField field) {
    super(field);
  }

  @Override
  public boolean visit(MethodInvocation node) {
    var expression = node.getExpression();
    if (expression != null
        && expression.getNodeType() == ASTNode.FIELD_ACCESS
        && ((FieldAccess) expression).resolveFieldBinding().getJavaElement().equals(field)) {
      switch (node.getName().getIdentifier()) {
        case "add": {
          var fieldWrite =
              new Writing(field, (Expression) node.arguments().get(0), Optional.empty());
          result.add(fieldWrite);
        }
      }
    }
    return true;
  }


}

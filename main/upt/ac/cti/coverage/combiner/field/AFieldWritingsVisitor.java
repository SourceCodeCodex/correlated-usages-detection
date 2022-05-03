package upt.ac.cti.coverage.combiner.field;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.ASTVisitor;
import upt.ac.cti.coverage.model.Writing;

abstract class AFieldWritingsVisitor extends ASTVisitor {
  protected final IField field;

  protected final List<Writing> result = new ArrayList<>();

  public AFieldWritingsVisitor(IField field) {
    this.field = field;
  }

  public List<Writing> fieldWritings() {
    return result;
  }
}

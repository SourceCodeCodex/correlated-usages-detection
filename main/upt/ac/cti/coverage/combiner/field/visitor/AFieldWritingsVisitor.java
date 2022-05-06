package upt.ac.cti.coverage.combiner.field.visitor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.ASTVisitor;
import upt.ac.cti.coverage.model.Writing;

public abstract class AFieldWritingsVisitor extends ASTVisitor {
  protected final IField field;
  protected final IMethod scope;

  protected final List<Writing<IField>> result = new ArrayList<>();

  public AFieldWritingsVisitor(IField field, IMethod scope) {
    this.field = field;
    this.scope = scope;
  }

  public List<Writing<IField>> fieldWritings() {
    return result;
  }
}

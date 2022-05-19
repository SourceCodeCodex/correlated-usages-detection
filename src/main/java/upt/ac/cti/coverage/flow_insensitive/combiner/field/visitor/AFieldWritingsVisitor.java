package upt.ac.cti.coverage.flow_insensitive.combiner.field.visitor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.ASTVisitor;
import upt.ac.cti.coverage.flow_insensitive.model.DerivableWriting;

public abstract class AFieldWritingsVisitor extends ASTVisitor {
  protected final IField field;

  protected final List<DerivableWriting<IField>> result = new ArrayList<>();

  public AFieldWritingsVisitor(IField field) {
    this.field = field;
  }

  public List<DerivableWriting<IField>> fieldWritings() {
    return result;
  }
}

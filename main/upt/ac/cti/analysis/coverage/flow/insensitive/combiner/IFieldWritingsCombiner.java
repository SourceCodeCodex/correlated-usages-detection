package upt.ac.cti.analysis.coverage.flow.insensitive.combiner;

import java.util.List;
import org.eclipse.jdt.core.IField;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;

public interface IFieldWritingsCombiner {
  public List<Pair<FieldWriting, FieldWriting>> combine(IField field1, IField field2);

}

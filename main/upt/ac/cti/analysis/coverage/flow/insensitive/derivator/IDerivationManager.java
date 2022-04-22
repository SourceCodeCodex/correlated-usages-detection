package upt.ac.cti.analysis.coverage.flow.insensitive.derivator;

import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;

public interface IDerivationManager {

  public Set<Pair<ITypeBinding, ITypeBinding>> derive(List<Pair<FieldWriting, FieldWriting>> input);

}

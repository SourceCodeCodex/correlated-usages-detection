package upt.ac.cti.core.pair.field.property;

import org.eclipse.jdt.core.IField;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.coverage.ICoveredTypesResolver;
import upt.ac.cti.dependency.Dependencies;

@PropertyComputer
public final class CoverageNameSimilarity extends Coverage
    implements IPropertyComputer<Integer, MFieldPair> {

  @Override
  protected ICoveredTypesResolver<IField> resolver() {
    return Dependencies.getNameSimilarityFieldCoveredTypesResolver();
  }



}

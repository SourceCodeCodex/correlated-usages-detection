package upt.ac.cti.core.pair.field.group;

import org.eclipse.jdt.core.IField;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MTypePair;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.coverage.ICoveredTypesResolver;
import upt.ac.cti.dependency.Dependencies;

@RelationBuilder
public class CoveredTypePairsNameSimilarity extends CoveredTypePairs
    implements IRelationBuilder<MTypePair, MFieldPair> {

  @Override
  protected ICoveredTypesResolver<IField> resolver() {
    return Dependencies.getNameSimilarityFieldCoveredTypesResolver();
  }

}

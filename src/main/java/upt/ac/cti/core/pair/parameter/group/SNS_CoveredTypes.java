package upt.ac.cti.core.pair.parameter.group;

import org.eclipse.jdt.core.ILocalVariable;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import familypolymorphismdetection.metamodel.entity.MTypePair;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.coverage.ICoveredTypesResolver;
import upt.ac.cti.dependency.Dependencies;

@RelationBuilder
public class SNS_CoveredTypes extends CoveredTypePairs
    implements IRelationBuilder<MTypePair, MParameterPair> {

  @Override
  protected ICoveredTypesResolver<ILocalVariable> resolver() {
    return Dependencies.squanderingNameSimilarityParameterCoveredTypesResolver;
  }


}

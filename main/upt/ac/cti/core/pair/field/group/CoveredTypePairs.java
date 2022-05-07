package upt.ac.cti.core.pair.field.group;

import static upt.ac.cti.dependencies.DependencyUtils.newFieldCoveredTypesResolver;
import org.eclipse.jdt.core.IField;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MTypePair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;

@RelationBuilder
public class CoveredTypePairs implements IRelationBuilder<MTypePair, MFieldPair> {

  @Override
  public Group<MTypePair> buildGroup(MFieldPair mFieldPair) {
    var group = new Group<MTypePair>();

    @SuppressWarnings("unchecked")
    var pair = (Pair<IField, IField>) mFieldPair.getUnderlyingObject();

    var resolver = newFieldCoveredTypesResolver();

    var factory = Factory.getInstance();

    var analysisResult = resolver.resolve(pair.getValue0(), pair.getValue1());

    if (analysisResult.isPresent()) {
      analysisResult.get().forEach(it -> group.add(factory.createMTypePair(it)));
    }

    return group;
  }
}

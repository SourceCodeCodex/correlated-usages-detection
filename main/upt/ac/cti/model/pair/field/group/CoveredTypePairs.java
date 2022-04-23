package upt.ac.cti.model.pair.field.group;

import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MTypePair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.analysis.coverage.flow.insensitive.CoverageAnalysis;

@RelationBuilder
public class CoveredTypePairs implements IRelationBuilder<MTypePair, MFieldPair> {


  @Override
  public Group<MTypePair> buildGroup(MFieldPair fieldPair) {
    var group = new Group<MTypePair>();
    var analysis = new CoverageAnalysis(fieldPair);

    var coveredTypes = analysis.coveredTypes();
    group.addAll(
        coveredTypes
            .stream()
            .map(ct -> Factory.getInstance().createMTypePair(ct))
            .toList());

    return group;
  }
}

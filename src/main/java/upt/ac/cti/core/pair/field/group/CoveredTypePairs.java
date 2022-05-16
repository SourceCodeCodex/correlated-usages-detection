package upt.ac.cti.core.pair.field.group;

import org.eclipse.jdt.core.IField;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MTypePair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import upt.ac.cti.coverage.ICoveredTypesResolver;

abstract class CoveredTypePairs {

  protected abstract ICoveredTypesResolver<IField> resolver();

  public Group<MTypePair> buildGroup(MFieldPair mFieldPair) {
    var group = new Group<MTypePair>();

    @SuppressWarnings("unchecked")
    var pair = (Pair<IField, IField>) mFieldPair.getUnderlyingObject();

    var resolver = resolver();

    var factory = Factory.getInstance();

    var analysisResult = resolver.resolve(pair.getValue0(), pair.getValue1());

    if (analysisResult.isPresent()) {
      analysisResult.get().forEach(it -> group.add(factory.createMTypePair(it)));
    }

    return group;
  }
}

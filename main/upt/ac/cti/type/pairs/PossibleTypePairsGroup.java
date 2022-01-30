package upt.ac.cti.type.pairs;

import java.util.List;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MTypePair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.utils.resolvers.BindingResolver;
import upt.ac.cti.utils.resolvers.ConcreteHierarchyTypesResolver;

@RelationBuilder
public class PossibleTypePairsGroup implements IRelationBuilder<MTypePair, MFieldPair> {

  @Override
  public Group<MTypePair> buildGroup(MFieldPair mFieldPair) {
    var typePairGroup = new Group<MTypePair>();
    @SuppressWarnings("unchecked")
    var iPair = (Pair<IField, IField>) mFieldPair.getUnderlyingObject();

    var field1TypesStream = getAllConcreteITypesForField(iPair.getValue0());
    var field2TypesStream = getAllConcreteITypesForField(iPair.getValue1());
    var typePairsStream = cartesianProduct(field1TypesStream, field2TypesStream);
    typePairsStream.forEach(it -> typePairGroup.add(Factory.getInstance().createMTypePair(it)));

    return typePairGroup;
  }

  private <T> List<Pair<T, T>> cartesianProduct(List<T> s1, List<T> s2) {
    return s1.stream().flatMap(it -> s2.stream().map(el -> Pair.with(it, el))).toList();
  }

  private List<IType> getAllConcreteITypesForField(IField iField) {
    var binding = BindingResolver.instance().resolveField(iField);
    var iType = (IType) binding.getJavaElement();
    return ConcreteHierarchyTypesResolver.instance().getConcreteInHierarchyTypes(iType);
  }

}

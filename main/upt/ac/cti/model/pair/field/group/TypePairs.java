package upt.ac.cti.model.pair.field.group;

import java.util.List;
import java.util.logging.Logger;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.javatuples.Pair;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.model.util.FieldBindingResolver;
import upt.ac.cti.util.HierarchyResolver;

@RelationBuilder
public final class TypePairs implements IRelationBuilder<MTypePair, MFieldPair> {

  private final FieldBindingResolver fieldTypeBindingResolver = new FieldBindingResolver();
  private final HierarchyResolver hierarchyResolver = new HierarchyResolver();

  private static final Logger logger = Logger.getLogger(TypePairs.class.getSimpleName());

  @Override
  public Group<MTypePair> buildGroup(MFieldPair mFieldPair) {
    logger.info(String.format("Group: %s - %s", this.getClass().getName(), mFieldPair));

    var group = new Group<MTypePair>();

    @SuppressWarnings("unchecked")
    var pair = (Pair<IField, IField>) mFieldPair.getUnderlyingObject();

    var f1Types = concreteTypes(pair.getValue0());
    var f2Types = concreteTypes(pair.getValue1());

    var typePairsStream = cartesianProduct(f1Types, f2Types);

    typePairsStream.forEach(it -> group.add(Factory.getInstance().createMTypePair(it)));

    return group;
  }

  private List<IType> concreteTypes(IField iField) {
    var binding = fieldTypeBindingResolver.resolve(iField);
    var iType = (IType) binding.getJavaElement();
    return hierarchyResolver.resolveConcreteDescendets(iType);
  }

  private <T> List<Pair<T, T>> cartesianProduct(List<T> s1, List<T> s2) {
    return s1.stream().flatMap(it -> s2.stream().map(el -> Pair.with(it, el))).toList();
  }


}

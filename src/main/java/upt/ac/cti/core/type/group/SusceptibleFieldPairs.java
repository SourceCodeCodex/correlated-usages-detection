package upt.ac.cti.core.type.group;

import java.util.List;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.validation.IsTypeBindingCollection;

@RelationBuilder
public final class SusceptibleFieldPairs implements IRelationBuilder<MFieldPair, MClass> {

  @Override
  public Group<MFieldPair> buildGroup(MClass mClass) {
    var group = new Group<MFieldPair>();
    var type = (IType) mClass.getUnderlyingObject();
    List<IField> fields;

    try {
      fields = List.of(type.getFields());
    } catch (JavaModelException e) {
      e.printStackTrace();

      fields = List.of();
    }

    var fieldTypeBindingResolver = Dependencies.fieldTypeBindingResolver;
    var fieldValidator = Dependencies.fieldValidator;

    var validFields = fields.stream()
        .filter(fieldValidator)
        .toList();

    var fieldsCount = validFields.size();

    var factory = Factory.getInstance();

    var isTBCollection = new IsTypeBindingCollection();

    for (var i = 0; i < fieldsCount; i++) {
      for (var j = i + 1; j < fieldsCount; j++) {
        var b1 = fieldTypeBindingResolver.resolve(validFields.get(i)).get();
        var b2 = fieldTypeBindingResolver.resolve(validFields.get(j)).get();

        if (b1.getJavaElement() == null || b2.getJavaElement() == null) {
          continue;
        }

        if (isTBCollection.test(b1)) {
          b1 = b1.getTypeArguments()[0];
          if (b1.getJavaElement() == null) {
            continue;
          }
        }

        if (isTBCollection.test(b2)) {
          b2 = b2.getTypeArguments()[0];
          if (b2.getJavaElement() == null) {
            continue;
          }
        }

        if (b1.getJavaElement().equals(b2.getJavaElement())) {
          continue;
        }


        var pair = Pair.with(validFields.get(i), validFields.get(j));
        group.add(factory.createMFieldPair(pair));

      }
    }

    return group;
  }
}

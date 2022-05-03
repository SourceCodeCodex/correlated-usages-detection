package upt.ac.cti.core.classes.group;

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
import upt.ac.cti.util.binding.FieldTypeBindingResolver;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;
import upt.ac.cti.util.validation.FieldValidator;

@RelationBuilder
public final class FieldPairs implements IRelationBuilder<MFieldPair, MClass> {

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

    var fieldValidator =
        new FieldValidator(new FieldTypeBindingResolver(), new ConcreteDescendantsResolver());

    var validFields = fields.parallelStream()
        .filter(fieldValidator)
        .toList();

    var fieldsCount = validFields.size();

    var factory = Factory.getInstance();

    for (var i = 0; i < fieldsCount; i++) {
      for (var j = i + 1; j < fieldsCount; j++) {
        var pair = Pair.with(validFields.get(i), validFields.get(j));
        group.add(factory.createMFieldPair(pair));
      }
    }

    return group;
  }
}

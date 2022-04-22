package upt.ac.cti.model.classes.group;

import java.util.List;
import java.util.logging.Logger;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.javatuples.Pair;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import upt.ac.cti.model.util.FieldBindingResolver;
import upt.ac.cti.util.HierarchyResolver;

@RelationBuilder
public final class FieldPairs implements IRelationBuilder<MFieldPair, MClass> {

  private static final Logger logger = Logger.getLogger(FieldPairs.class.getSimpleName());

  @Override
  public Group<MFieldPair> buildGroup(MClass mClass) {
    logger.info(String.format("Group: %s - %s", this.getClass().getName(), mClass));

    var group = new Group<MFieldPair>();
    var type = (IType) mClass.getUnderlyingObject();
    List<IField> fields;

    try {
      fields = List.of(type.getFields());
    } catch (JavaModelException e) {
      var ste = e.getStackTrace()[0];
      logger.throwing(ste.getClassName(), ste.getMethodName(), e);
      fields = List.of();
    }

    var validFields = fields.stream()
        .filter(field -> {
          var ftbr = new FieldBindingResolver();
          var hr = new HierarchyResolver();
          var validator = new FieldValidator(ftbr, hr);
          return validator.isValid(field);
        })
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

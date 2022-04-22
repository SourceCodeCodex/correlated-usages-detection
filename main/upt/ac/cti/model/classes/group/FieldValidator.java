package upt.ac.cti.model.classes.group;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import upt.ac.cti.model.util.FieldBindingResolver;
import upt.ac.cti.util.HierarchyResolver;

class FieldValidator {

  private static final Logger logger = Logger.getLogger(FieldValidator.class.getSimpleName());

  private final FieldBindingResolver fieldTypeBindingResolver;
  private final HierarchyResolver hierarchyResolver;

  public FieldValidator(FieldBindingResolver fieldTypeBindingResolver,
      HierarchyResolver hierarchyResolver) {
    this.fieldTypeBindingResolver = fieldTypeBindingResolver;
    this.hierarchyResolver = hierarchyResolver;
  }

  public boolean isValid(IField field) {
    return !filters()
        .stream()
        .anyMatch(p -> p.test(field));
  }


  private List<Predicate<IField>> filters() {

    return List.of(
        isStatic(),
        isPrimitive(),
        isArray(),
        isParameterizedType(),
        notFromSource(),
        refTypeHasNoHierarchy());
  }

  private final Predicate<IField> isStatic() {
    return field -> {
      try {
        return Flags.isStatic(field.getFlags());
      } catch (JavaModelException e) {
        var ste = e.getStackTrace()[0];
        logger.throwing(ste.getClassName(), ste.getMethodName(), e);
        return false;
      }
    };
  }

  private final Predicate<IField> isPrimitive() {
    return field -> fieldTypeBindingResolver.resolve(field).isPrimitive();
  }


  private final Predicate<IField> isArray() {
    return field -> fieldTypeBindingResolver.resolve(field).isArray();
  }



  private final Predicate<IField> isParameterizedType() {
    return field -> fieldTypeBindingResolver.resolve(field).isParameterizedType();
  }



  private final Predicate<IField> notFromSource() {
    return field -> !fieldTypeBindingResolver.resolve(field).isFromSource();
  }


  private final Predicate<IField> refTypeHasNoHierarchy() {
    return field -> {
      var type = (IType) fieldTypeBindingResolver.resolve(field).getJavaElement();
      return hierarchyResolver.resolveConcreteDescendets(type).size() < 2;
    };
  }


}


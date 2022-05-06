package upt.ac.cti.dependencies;

import upt.ac.cti.aperture.FieldAllTypePairsResolver;
import upt.ac.cti.aperture.ParameterAllTypePairsResolver;
import upt.ac.cti.coverage.FieldCoveredTypesResolver;
import upt.ac.cti.coverage.ParameterCoveredTypesResolver;
import upt.ac.cti.util.binding.FieldTypeBindingResolver;
import upt.ac.cti.util.binding.ParameterTypeBindingResolver;
import upt.ac.cti.util.hierarchy.ConcreteDescendantsResolver;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;
import upt.ac.cti.util.validation.FieldValidator;
import upt.ac.cti.util.validation.ParameterValidator;
import upt.ac.cti.util.validation.SusceptibleTypeValidator;

public class DependencyUtils {

  /* Hierarchy */
  public static ConcreteDescendantsResolver newConcreteDescendantsResolver() {
    return new ConcreteDescendantsResolver();
  }

  /* Binding */
  public static FieldTypeBindingResolver newFieldTypeBindingResolver() {
    return new FieldTypeBindingResolver();
  }

  public static ParameterTypeBindingResolver newParameterTypeBindingResolver() {
    return new ParameterTypeBindingResolver();
  }

  /* Validation */
  public static SusceptibleTypeValidator newSusceptibleTypeValidator() {
    return new SusceptibleTypeValidator();
  }

  public static FieldValidator newFieldValidator() {
    return new FieldValidator(newFieldTypeBindingResolver(), newConcreteDescendantsResolver());
  }

  public static FieldValidator newFieldValidator(
      FieldTypeBindingResolver fieldTypeBindingResolver) {
    return new FieldValidator(fieldTypeBindingResolver, newConcreteDescendantsResolver());
  }

  public static FieldValidator newFieldValidator(
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    return new FieldValidator(newFieldTypeBindingResolver(), concreteDescendantsResolver);
  }

  public static FieldValidator newFieldValidator(
      FieldTypeBindingResolver fieldTypeBindingResolver,
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    return new FieldValidator(fieldTypeBindingResolver, concreteDescendantsResolver);
  }

  public static ParameterValidator newParameterValidator() {
    return new ParameterValidator(newParameterTypeBindingResolver(),
        newConcreteDescendantsResolver());
  }

  public static ParameterValidator newParameterValidator(
      ParameterTypeBindingResolver parameterTypeBindingResolver) {
    return new ParameterValidator(parameterTypeBindingResolver, newConcreteDescendantsResolver());
  }

  public static ParameterValidator newParameterValidator(
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    return new ParameterValidator(newParameterTypeBindingResolver(), concreteDescendantsResolver);
  }

  public static ParameterValidator newParameterValidator(
      ParameterTypeBindingResolver parameterTypeBindingResolver,
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    return new ParameterValidator(parameterTypeBindingResolver, concreteDescendantsResolver);
  }

  /* Aperture */
  public static FieldAllTypePairsResolver newFieldAllTypePairsResolver() {
    return new FieldAllTypePairsResolver(newFieldTypeBindingResolver(),
        newConcreteDescendantsResolver());
  }

  public static FieldAllTypePairsResolver newFieldAllTypePairsResolver(
      FieldTypeBindingResolver fieldTypeBindingResolver) {
    return new FieldAllTypePairsResolver(fieldTypeBindingResolver,
        newConcreteDescendantsResolver());
  }

  public static FieldAllTypePairsResolver newFieldAllTypePairsResolver(
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    return new FieldAllTypePairsResolver(newFieldTypeBindingResolver(),
        concreteDescendantsResolver);
  }

  public static FieldAllTypePairsResolver newFieldAllTypePairsResolver(
      FieldTypeBindingResolver fieldTypeBindingResolver,
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    return new FieldAllTypePairsResolver(fieldTypeBindingResolver, concreteDescendantsResolver);
  }

  public static ParameterAllTypePairsResolver newParameterAllTypePairsResolver() {
    return new ParameterAllTypePairsResolver(newParameterTypeBindingResolver(),
        newConcreteDescendantsResolver());
  }

  public static ParameterAllTypePairsResolver newParameterAllTypePairsResolver(
      ParameterTypeBindingResolver parameterTypeBindingResolver) {
    return new ParameterAllTypePairsResolver(parameterTypeBindingResolver,
        newConcreteDescendantsResolver());
  }

  public static ParameterAllTypePairsResolver newParameterAllTypePairsResolver(
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    return new ParameterAllTypePairsResolver(newParameterTypeBindingResolver(),
        concreteDescendantsResolver);
  }

  public static ParameterAllTypePairsResolver newParameterAllTypePairsResolver(
      ParameterTypeBindingResolver parameterTypeBindingResolver,
      ConcreteDescendantsResolver concreteDescendantsResolver) {
    return new ParameterAllTypePairsResolver(parameterTypeBindingResolver,
        concreteDescendantsResolver);
  }

  /* Coverage */

  public static FieldCoveredTypesResolver newFieldCoveredTypesResolver() {
    return new FieldCoveredTypesResolver(
        newCodeParser(),
        newJavaEntitySearcher(),
        newFieldTypeBindingResolver(),
        newConcreteDescendantsResolver());
  }

  public static ParameterCoveredTypesResolver newParameterCoveredTypesResolver() {
    return new ParameterCoveredTypesResolver(
        newCodeParser(),
        newJavaEntitySearcher(),
        newParameterTypeBindingResolver(),
        newConcreteDescendantsResolver());
  }

  /* Parsing */
  public static CodeParser newCodeParser() {
    return new CodeParser();
  }

  /* Search */
  public static JavaEntitySearcher newJavaEntitySearcher() {
    return new JavaEntitySearcher();
  }
}

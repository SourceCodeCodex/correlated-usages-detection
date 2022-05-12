package upt.ac.cti.dependency;

import upt.ac.cti.aperture.FieldAllTypePairsResolver;
import upt.ac.cti.aperture.ParameterAllTypePairsResolver;
import upt.ac.cti.config.Config;
import upt.ac.cti.coverage.FieldCoveredTypesResolver;
import upt.ac.cti.coverage.ParameterCoveredTypesResolver;
import upt.ac.cti.coverage.derivator.util.FieldWritingBindingResolver;
import upt.ac.cti.coverage.derivator.util.ParameterWritingBindingResolver;
import upt.ac.cti.util.binding.FieldTypeBindingResolver;
import upt.ac.cti.util.binding.ParameterTypeBindingResolver;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.hierarchy.HierarchyResolver;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;
import upt.ac.cti.util.validation.FieldValidator;
import upt.ac.cti.util.validation.ParameterValidator;
import upt.ac.cti.util.validation.SusceptibleTypeValidator;

public class Dependencies {

  private static Config config;

  private static FieldAllTypePairsResolver fieldAllTypePairsResolver;
  private static ParameterAllTypePairsResolver parameterAllTypePairsResolver;

  private static FieldCoveredTypesResolver fieldCoveredTypesResolver;
  private static ParameterCoveredTypesResolver parameterCoveredTypesResolver;

  private static HierarchyResolver hierarchyResolver;
  private static CodeParser codeParser;
  private static JavaEntitySearcher javaEntitySearcher;

  private static FieldTypeBindingResolver fieldTypeBindingResolver;
  private static ParameterTypeBindingResolver parameterTypeBindingResolver;

  private static FieldValidator fieldValidator;
  private static ParameterValidator parameterValidator;
  private static SusceptibleTypeValidator susceptibleTypeValidator;

  private static FieldWritingBindingResolver fieldWritingBindingResolver;
  private static ParameterWritingBindingResolver parameterWritingBindingResolver;

  public static void init(Config config) {
    Cache.clearAllCache();
    Dependencies.config = config;
    hierarchyResolver = new HierarchyResolver();
    codeParser = new CodeParser();
    javaEntitySearcher = new JavaEntitySearcher();
    fieldTypeBindingResolver = new FieldTypeBindingResolver();
    parameterTypeBindingResolver = new ParameterTypeBindingResolver();
    fieldValidator = new FieldValidator(fieldTypeBindingResolver, hierarchyResolver);
    parameterValidator = new ParameterValidator(parameterTypeBindingResolver, hierarchyResolver);
    susceptibleTypeValidator = new SusceptibleTypeValidator();
    fieldAllTypePairsResolver =
        new FieldAllTypePairsResolver(fieldTypeBindingResolver, hierarchyResolver);
    parameterAllTypePairsResolver =
        new ParameterAllTypePairsResolver(parameterTypeBindingResolver,
            hierarchyResolver);
    fieldWritingBindingResolver =
        new FieldWritingBindingResolver(hierarchyResolver, fieldTypeBindingResolver);
    parameterWritingBindingResolver =
        new ParameterWritingBindingResolver(hierarchyResolver, parameterTypeBindingResolver);

    fieldCoveredTypesResolver = new FieldCoveredTypesResolver(codeParser, javaEntitySearcher,
        fieldWritingBindingResolver, fieldAllTypePairsResolver);

    parameterCoveredTypesResolver = new ParameterCoveredTypesResolver(codeParser,
        javaEntitySearcher, parameterWritingBindingResolver,
        parameterAllTypePairsResolver);
  }

  public static Config getConfig() {
    return config;
  }

  public static FieldAllTypePairsResolver getFieldAllTypePairsResolver() {
    return fieldAllTypePairsResolver;
  }

  public static ParameterAllTypePairsResolver getParameterAllTypePairsResolver() {
    return parameterAllTypePairsResolver;
  }

  public static FieldCoveredTypesResolver getFieldCoveredTypesResolver() {
    return fieldCoveredTypesResolver;
  }

  public static ParameterCoveredTypesResolver getParameterCoveredTypesResolver() {
    return parameterCoveredTypesResolver;
  }

  public static HierarchyResolver getHierarchyResolver() {
    return hierarchyResolver;
  }

  public static CodeParser getCodeParser() {
    return codeParser;
  }

  public static JavaEntitySearcher getJavaEntitySearcher() {
    return javaEntitySearcher;
  }

  public static FieldTypeBindingResolver getFieldTypeBindingResolver() {
    return fieldTypeBindingResolver;
  }

  public static ParameterTypeBindingResolver getParameterTypeBindingResolver() {
    return parameterTypeBindingResolver;
  }

  public static FieldValidator getFieldValidator() {
    return fieldValidator;
  }

  public static ParameterValidator getParameterValidator() {
    return parameterValidator;
  }

  public static SusceptibleTypeValidator getSusceptibleTypeValidator() {
    return susceptibleTypeValidator;
  }


}

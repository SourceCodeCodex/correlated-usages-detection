package upt.ac.cti.dependency;

import upt.ac.cti.aperture.FieldAllTypePairsResolver;
import upt.ac.cti.aperture.ParameterAllTypePairsResolver;
import upt.ac.cti.coverage.flow_insensitive.FlowInsensitiveFieldCoveredTypesResolver;
import upt.ac.cti.coverage.flow_insensitive.FlowInsensitiveParameterCoveredTypesResolver;
import upt.ac.cti.coverage.flow_insensitive.derivator.util.FieldWritingBindingResolver;
import upt.ac.cti.coverage.flow_insensitive.derivator.util.ParameterWritingBindingResolver;
import upt.ac.cti.coverage.name_similarity.NameSimilarityFieldCoveredTypesResolver;
import upt.ac.cti.coverage.name_similarity.NameSimilarityParameterCoveredTypesResolver;
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

  private static FieldAllTypePairsResolver fieldAllTypePairsResolver;
  private static ParameterAllTypePairsResolver parameterAllTypePairsResolver;

  private static FlowInsensitiveFieldCoveredTypesResolver flowInsensitiveFieldCoveredTypesResolver;
  private static FlowInsensitiveParameterCoveredTypesResolver flowInsensitiveParameterCoveredTypesResolver;

  private static NameSimilarityFieldCoveredTypesResolver nameSimilarityFieldCoveredTypesResolver;
  private static NameSimilarityParameterCoveredTypesResolver nameSimilarityParameterCoveredTypesResolver;

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

  public static void init() {
    Cache.clearAllCache();
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

    flowInsensitiveFieldCoveredTypesResolver =
        new FlowInsensitiveFieldCoveredTypesResolver(codeParser, javaEntitySearcher,
            fieldWritingBindingResolver, fieldAllTypePairsResolver);

    flowInsensitiveParameterCoveredTypesResolver =
        new FlowInsensitiveParameterCoveredTypesResolver(codeParser,
            javaEntitySearcher, parameterWritingBindingResolver,
            parameterAllTypePairsResolver);

    nameSimilarityFieldCoveredTypesResolver =
        new NameSimilarityFieldCoveredTypesResolver(
            fieldAllTypePairsResolver, fieldTypeBindingResolver);

    nameSimilarityParameterCoveredTypesResolver =
        new NameSimilarityParameterCoveredTypesResolver(parameterAllTypePairsResolver,
            parameterTypeBindingResolver);
  }


  public static FieldAllTypePairsResolver getFieldAllTypePairsResolver() {
    return fieldAllTypePairsResolver;
  }

  public static ParameterAllTypePairsResolver getParameterAllTypePairsResolver() {
    return parameterAllTypePairsResolver;
  }

  public static FlowInsensitiveFieldCoveredTypesResolver getFlowInsensitiveFieldCoveredTypesResolver() {
    return flowInsensitiveFieldCoveredTypesResolver;
  }

  public static FlowInsensitiveParameterCoveredTypesResolver getFlowInsensitiveParameterCoveredTypesResolver() {
    return flowInsensitiveParameterCoveredTypesResolver;
  }

  public static NameSimilarityFieldCoveredTypesResolver getNameSimilarityFieldCoveredTypesResolver() {
    return nameSimilarityFieldCoveredTypesResolver;
  }

  public static NameSimilarityParameterCoveredTypesResolver getNameSimilarityParameterCoveredTypesResolver() {
    return nameSimilarityParameterCoveredTypesResolver;
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

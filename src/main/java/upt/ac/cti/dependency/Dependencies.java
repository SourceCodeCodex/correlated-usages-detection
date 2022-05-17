package upt.ac.cti.dependency;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.ILocalVariable;
import upt.ac.cti.aperture.FieldAllTypePairsResolver;
import upt.ac.cti.aperture.ParameterAllTypePairsResolver;
import upt.ac.cti.coverage.flow_insensitive.ConservingFlowInsensitiveFieldCoveredTypesResolver;
import upt.ac.cti.coverage.flow_insensitive.ConservingFlowInsensitiveParameterCoveredTypesResolver;
import upt.ac.cti.coverage.flow_insensitive.SquanderingFlowInsensitiveFieldCoveredTypesResolver;
import upt.ac.cti.coverage.flow_insensitive.SquanderingFlowInsensitiveParameterCoveredTypesResolver;
import upt.ac.cti.coverage.flow_insensitive.combiner.field.FieldWritingsCombiner;
import upt.ac.cti.coverage.flow_insensitive.combiner.parameter.ParameterWritingsCombiner;
import upt.ac.cti.coverage.flow_insensitive.derivator.ConservingDerivationManager;
import upt.ac.cti.coverage.flow_insensitive.derivator.SquanderingDerivationManager;
import upt.ac.cti.coverage.flow_insensitive.derivator.util.FieldWritingBindingResolver;
import upt.ac.cti.coverage.flow_insensitive.derivator.util.ParameterWritingBindingResolver;
import upt.ac.cti.coverage.name_similarity.ConservingNameSimilarityFieldCoveredTypesResolver;
import upt.ac.cti.coverage.name_similarity.ConservingNameSimilarityParameterCoveredTypesResolver;
import upt.ac.cti.coverage.name_similarity.SquanderingNameSimilarityFieldCoveredTypesResolver;
import upt.ac.cti.coverage.name_similarity.SquanderingNameSimilarityParameterCoveredTypesResolver;
import upt.ac.cti.util.binding.FieldTypeBindingResolver;
import upt.ac.cti.util.binding.ParameterTypeBindingResolver;
import upt.ac.cti.util.cache.Cache;
import upt.ac.cti.util.hierarchy.HierarchyResolver;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;
import upt.ac.cti.util.validation.FieldValidator;
import upt.ac.cti.util.validation.IsTypeBindingCollection;
import upt.ac.cti.util.validation.ParameterValidator;
import upt.ac.cti.util.validation.SusceptibleTypeValidator;

/**
 * Mind that the order of declaration should not be changed! This code is very dangerous! KEEP IN
 * MIND THAT ALL COMPONENTS ARE !!!STATELESS!! If a component will have state, there will be bugs!
 */
public class Dependencies {

  // Util
  public static IsTypeBindingCollection isTypeBindingCollection = new IsTypeBindingCollection();
  public static HierarchyResolver hierarchyResolver = new HierarchyResolver();
  public static CodeParser codeParser = new CodeParser();
  public static JavaEntitySearcher javaEntitySearcher = new JavaEntitySearcher();

  // Core Bindings
  public static FieldTypeBindingResolver fieldTypeBindingResolver =
      new FieldTypeBindingResolver();
  public static ParameterTypeBindingResolver parameterTypeBindingResolver =
      new ParameterTypeBindingResolver();

  // Validation
  public static FieldValidator fieldValidator = new FieldValidator();
  public static ParameterValidator parameterValidator = new ParameterValidator();
  public static SusceptibleTypeValidator typeValidator = new SusceptibleTypeValidator();

  // All Type Pairs
  public static FieldAllTypePairsResolver fieldAllTypePairsResolver =
      new FieldAllTypePairsResolver(fieldTypeBindingResolver);
  public static ParameterAllTypePairsResolver parameterAllTypePairsResolver =
      new ParameterAllTypePairsResolver(parameterTypeBindingResolver);

  // Combiners
  public static FieldWritingsCombiner fieldWritingsCombiner =
      new FieldWritingsCombiner();
  public static ParameterWritingsCombiner parameterWritingsCombiner =
      new ParameterWritingsCombiner();

  // Writing Bindings - Derivation
  public static FieldWritingBindingResolver fieldWritingBindingResolver =
      new FieldWritingBindingResolver();
  public static ParameterWritingBindingResolver parameterWritingBindingResolver =
      new ParameterWritingBindingResolver();

  // Derivation Managers //

  // Fields
  public static ConservingDerivationManager<IField> fieldConservingDerivationManager =
      new ConservingDerivationManager<>(fieldWritingBindingResolver, fieldAllTypePairsResolver);
  public static SquanderingDerivationManager<IField> fieldSquanderingDerivationManager =
      new SquanderingDerivationManager<>(fieldWritingBindingResolver, fieldAllTypePairsResolver);

  // Parameter
  public static ConservingDerivationManager<ILocalVariable> parameterConservingDerivationManager =
      new ConservingDerivationManager<>(parameterWritingBindingResolver,
          parameterAllTypePairsResolver);
  public static SquanderingDerivationManager<ILocalVariable> parameterSquanderingDerivationManager =
      new SquanderingDerivationManager<>(parameterWritingBindingResolver,
          parameterAllTypePairsResolver);

  // Covered Types Resolvers

  // Flow insensitive

  // Fields
  public static ConservingFlowInsensitiveFieldCoveredTypesResolver conservingFlowInsensitiveFieldCoveredTypesResolver =
      new ConservingFlowInsensitiveFieldCoveredTypesResolver();
  public static SquanderingFlowInsensitiveFieldCoveredTypesResolver squanderingFlowInsensitiveFieldCoveredTypesResolver =
      new SquanderingFlowInsensitiveFieldCoveredTypesResolver();

  // Parameters
  public static ConservingFlowInsensitiveParameterCoveredTypesResolver conservingFlowInsensitiveParameterCoveredTypesResolver =
      new ConservingFlowInsensitiveParameterCoveredTypesResolver();
  public static SquanderingFlowInsensitiveParameterCoveredTypesResolver squanderingFlowInsensitiveParameterCoveredTypesResolver =
      new SquanderingFlowInsensitiveParameterCoveredTypesResolver();

  // Name Similarity

  // Fields
  public static ConservingNameSimilarityFieldCoveredTypesResolver conservingNameSimilarityFieldCoveredTypesResolver =
      new ConservingNameSimilarityFieldCoveredTypesResolver();;
  public static SquanderingNameSimilarityFieldCoveredTypesResolver squanderingNameSimilarityFieldCoveredTypesResolver =
      new SquanderingNameSimilarityFieldCoveredTypesResolver();

  // Parameters
  public static ConservingNameSimilarityParameterCoveredTypesResolver conservingNameSimilarityParameterCoveredTypesResolver =
      new ConservingNameSimilarityParameterCoveredTypesResolver();
  public static SquanderingNameSimilarityParameterCoveredTypesResolver squanderingNameSimilarityParameterCoveredTypesResolver =
      new SquanderingNameSimilarityParameterCoveredTypesResolver();


  public synchronized static void init() {
    Cache.clearAllCache();

    // Util
    isTypeBindingCollection = new IsTypeBindingCollection();
    hierarchyResolver = new HierarchyResolver();
    codeParser = new CodeParser();
    javaEntitySearcher = new JavaEntitySearcher();

    // Core Bindings
    fieldTypeBindingResolver = new FieldTypeBindingResolver();
    parameterTypeBindingResolver = new ParameterTypeBindingResolver();

    // Validation
    fieldValidator = new FieldValidator();
    parameterValidator = new ParameterValidator();
    typeValidator = new SusceptibleTypeValidator();

    // All Type Pairs
    fieldAllTypePairsResolver =
        new FieldAllTypePairsResolver(fieldTypeBindingResolver);
    parameterAllTypePairsResolver =
        new ParameterAllTypePairsResolver(parameterTypeBindingResolver);

    // Combiners
    fieldWritingsCombiner = new FieldWritingsCombiner();
    parameterWritingsCombiner = new ParameterWritingsCombiner();

    // Writing Bindings - Derivation
    fieldWritingBindingResolver =
        new FieldWritingBindingResolver();
    parameterWritingBindingResolver =
        new ParameterWritingBindingResolver();

    // Derivation Managers //

    // Fields
    fieldConservingDerivationManager =
        new ConservingDerivationManager<>(fieldWritingBindingResolver, fieldAllTypePairsResolver);
    fieldSquanderingDerivationManager =
        new SquanderingDerivationManager<>(fieldWritingBindingResolver, fieldAllTypePairsResolver);

    // Parameter
    parameterConservingDerivationManager =
        new ConservingDerivationManager<>(parameterWritingBindingResolver,
            parameterAllTypePairsResolver);
    parameterSquanderingDerivationManager =
        new SquanderingDerivationManager<>(parameterWritingBindingResolver,
            parameterAllTypePairsResolver);

    // Covered Types Resolvers

    // Flow insensitive

    // Fields
    conservingFlowInsensitiveFieldCoveredTypesResolver =
        new ConservingFlowInsensitiveFieldCoveredTypesResolver();
    squanderingFlowInsensitiveFieldCoveredTypesResolver =
        new SquanderingFlowInsensitiveFieldCoveredTypesResolver();

    // Parameters
    conservingFlowInsensitiveParameterCoveredTypesResolver =
        new ConservingFlowInsensitiveParameterCoveredTypesResolver();
    squanderingFlowInsensitiveParameterCoveredTypesResolver =
        new SquanderingFlowInsensitiveParameterCoveredTypesResolver();

    // Name Similarity

    // Fields
    conservingNameSimilarityFieldCoveredTypesResolver =
        new ConservingNameSimilarityFieldCoveredTypesResolver();;
    squanderingNameSimilarityFieldCoveredTypesResolver =
        new SquanderingNameSimilarityFieldCoveredTypesResolver();

    // Parameters
    conservingNameSimilarityParameterCoveredTypesResolver =
        new ConservingNameSimilarityParameterCoveredTypesResolver();
    squanderingNameSimilarityParameterCoveredTypesResolver =
        new SquanderingNameSimilarityParameterCoveredTypesResolver();
  }

}

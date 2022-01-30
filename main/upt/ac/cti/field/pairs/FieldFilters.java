package upt.ac.cti.field.pairs;

import java.util.List;
import java.util.function.Predicate;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import upt.ac.cti.utils.resolvers.BindingResolver;


/**
 * This class bundles all filters applied at the phase of pairing fields together.
 *
 * @author alinrosu
 *
 */
class FieldFilters {

  private FieldFilters() {

  }

  public static List<Predicate<IField>> filters() {
    return List.of(
        isFieldStatic,
        isPrimitive,
        isArray,
        isGeneric);
  }

  private static final Predicate<IField> isFieldStatic = iField -> {
    try {
      return Flags.isStatic(iField.getFlags());
    } catch (JavaModelException e) {
      e.printStackTrace();
      return false;
    }
  };

  private static final Predicate<IField> isPrimitive =
      iField -> BindingResolver.instance().resolveField(iField).isPrimitive();

  private static final Predicate<IField> isArray =
      iField -> BindingResolver.instance().resolveField(iField).isArray();

  private static final Predicate<IField> isGeneric =
      iField -> BindingResolver.instance().resolveField(iField).isGenericType();


}

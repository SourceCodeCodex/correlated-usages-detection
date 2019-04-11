package upt.se.utils.helpers;

import org.eclipse.jdt.core.dom.ITypeBinding;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MTypeParameter;

public class ClassNames {
  public static final String OBJECT_NAME = Object.class.getCanonicalName();

  public static boolean isObject(MClass entity) {
    return entity.getUnderlyingObject().getFullyQualifiedName().equals(OBJECT_NAME);
  }

  public static boolean isObject(String entity) {
    return entity.equals(OBJECT_NAME);
  }
  
  public static boolean isEqual(ITypeBinding type1, ITypeBinding type2) {
    return type1.getQualifiedName().equals(type2.getQualifiedName());
  }

  public static String getName(MClass entity) {
    return entity.getUnderlyingObject().getElementName();
  }

  public static String getName(MTypeParameter entity) {
    return entity.getUnderlyingObject().getJavaElement().getElementName();
  }

  public static String getFullName(ITypeBinding entity) {
    return entity.getQualifiedName();
  }
}

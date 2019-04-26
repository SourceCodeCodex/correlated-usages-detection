package upt.se.utils.helpers;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.Tuple2;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MArgumentType;

public class ClassNames {
  public static final String OBJECT_NAME = Object.class.getCanonicalName();

  public static boolean isObject(MClass entity) {
    return entity.getUnderlyingObject().getFullyQualifiedName().equals(OBJECT_NAME);
  }

  public static boolean isObject(MArgumentType entity) {
    return entity.getUnderlyingObject().getQualifiedName().equals(OBJECT_NAME);
  }
  
  public static boolean parentExtendsObject(MArgumentType entity) {
    return entity.getUnderlyingObject().getSuperclass().getQualifiedName().equals(OBJECT_NAME);
  }

  public static boolean isObject(String entity) {
    return entity.equals(OBJECT_NAME);
  }

  public static boolean isObject(ITypeBinding entity) {
    return entity.getQualifiedName().equals(OBJECT_NAME);
  }

  public static boolean isEqual(ITypeBinding type1, ITypeBinding type2) {
    return type1.getQualifiedName().equals(type2.getQualifiedName());
  }


  public static boolean isEqual(IType type1, ITypeBinding type2) {
    return type1.getFullyQualifiedName().equals(type2.getQualifiedName());
  }
  
  public static boolean isEqual(Tuple2<ITypeBinding, ITypeBinding> tuple1,
      Tuple2<ITypeBinding, ITypeBinding> tuple2) {
    return isEqual(tuple1._1, tuple2._1) && isEqual(tuple1._2, tuple2._2)
        || isEqual(tuple1._1, tuple2._2) && isEqual(tuple1._2, tuple2._1);
  }

  public static String getName(MClass entity) {
    return entity.getUnderlyingObject().getElementName();
  }

  public static String getName(MArgumentType entity) {
    return entity.getUnderlyingObject().getJavaElement().getElementName();
  }

  public static String getFullName(ITypeBinding entity) {
    return entity.getQualifiedName();
  }
}

package upt.se.utils.helpers;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.Tuple2;
import thesis.metamodel.entity.MArgument;
import thesis.metamodel.entity.MParameter;

public class Equals {
  public static final String OBJECT_NAME = Object.class.getCanonicalName();

  public static boolean isObject(MArgument entity) {
    return entity.getUnderlyingObject().getFullyQualifiedName().equals(OBJECT_NAME);
  }

  public static boolean isObject(MParameter entity) {
    return isObject(entity.getUnderlyingObject());
  }
  
  public static boolean parentExtendsObject(MParameter entity) {
    return isObject(entity.getUnderlyingObject().getSuperclass());
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

  public static  boolean isEqual(IType type1, IType type2) {
    return type1.getFullyQualifiedName().equals(type2.getFullyQualifiedName());
  }

  public static boolean isEqual(IType type1, ITypeBinding type2) {
    return type1.getFullyQualifiedName().equals(type2.getBinaryName());
  }
  
  public static boolean isEqual(Tuple2<IType, IType> tuple1,
      Tuple2<IType, IType> tuple2) {
    return isEqual(tuple1._1, tuple2._1) && isEqual(tuple1._2, tuple2._2)
        || isEqual(tuple1._1, tuple2._2) && isEqual(tuple1._2, tuple2._1);
  }

}

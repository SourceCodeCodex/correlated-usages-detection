package upt.se.utils.helpers;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.XEntity;
import thesis.metamodel.entity.MArgumentType;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MTypePair;
import upt.se.utils.TypePair;

public class GroupBuilder {

  public static <T extends XEntity> Group<T> wrap(List<T> list) {
    Group<T> group = new Group<>();
    group.addAll(list.asJava());

    return group;
  }

  public static List<ITypeBinding> unwrapArguments(Group<MArgumentType> group) {
    return List.ofAll(group.getElements()).map(MArgumentType::getUnderlyingObject);
  }

  public static List<IType> unwrapClass(Group<MClass> group) {
    return List.ofAll(group.getElements()).map(MClass::getUnderlyingObject);
  }
  
  public static List<TypePair> unwrapTypePairs(Group<MTypePair> group) {
    return List.ofAll(group.getElements()).map(MTypePair::getUnderlyingObject);
  }
}

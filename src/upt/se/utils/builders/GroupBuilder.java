package upt.se.utils.builders;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.XEntity;
import thesis.metamodel.entity.MTypeParameter;
import static upt.se.utils.builders.ListBuilder.*;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class GroupBuilder {

  public static <T extends XEntity> Group<T> wrap(java.util.List<T> list) {
    Group<T> group = new Group<>();
    group.addAll(list);

    return group;
  }

  public static java.util.List<ITypeBinding> unwrap(Group<MTypeParameter> group) {
    return toList(group.getElements()).map(MTypeParameter::getUnderlyingObject).asJava();
  }
  
}
